package com.skyapi.weatherforecast.realtime;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Date;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.skyapi.weatherforecast.common.Location;
import com.skyapi.weatherforecast.common.RealtimeWeather;
import com.skyapi.weatherforecast.location.exceptions.GeoLocationException;
import com.skyapi.weatherforecast.location.exceptions.LocationNotFoundException;
import com.skyapi.weatherforecast.location.service.GeoLocationService;
import com.skyapi.weatherforecast.realtime.service.RealtimeWeatherService;
import com.skyapi.weatherforecast.realtime.web.RealtimeWeatherController;
import com.skyapi.weatherforecast.realtime.web.dto.RealtimeWeatherDTO;

@WebMvcTest(RealtimeWeatherController.class)
public class RealtimeWeatherAPIControllerTest {

	
	private static final String END_POINT_PATH = "/v1/realtime";
	private static final String RESPONSE_CONTENT_TYPE = "application/hal+json";
	private static final String REQUEST_CONTENT_TYPE = "application/json";

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	ObjectMapper mapper;
	
	@MockBean 
	RealtimeWeatherService realtimeWeatherService;
	
	@MockBean 
	GeoLocationService locationService;
	
	@MockBean 
    private ModelMapper modelMapper;
	
	
	@Test
	public void testGetShouldReturn400BadRequest() throws Exception {
		Mockito.when(locationService.getLocation(Mockito.anyString())).thenThrow(GeoLocationException.class);
		
		mockMvc.perform(get(END_POINT_PATH))
		.andExpect(status().isBadRequest())
		.andDo(print());
	}
	
	
	@Test
	public void testGetShouldReturn404NotFound() throws Exception {
		
		Location location = new Location();
		location.setCountryCode("US");
		location.setCityName("Town");
		
		LocationNotFoundException exception = new LocationNotFoundException(location.getCountryCode(), location.getCityName());
		
		Mockito.when(locationService.getLocation(Mockito.anyString())).thenReturn(location);
		Mockito.when(realtimeWeatherService.getByLocation(location)).thenThrow(exception);
		
		
		mockMvc.perform(get(END_POINT_PATH))
		.andExpect(status().isNotFound())
		.andExpect(jsonPath("$.errors[0]", is (exception.getMessage())))
		.andDo(print());
	}
	
	
	
	@Test
	public void testGetShouldReturnStatus200Ok() throws Exception {
		
		Location location = new Location();
		location.setCode("NYC_USA");
		location.setCityName("New York City");
		location.setRegionName("New York");
		location.setCountryName("USA");
		location.setCountryCode("US");
		
		RealtimeWeather realtimeWeather = new RealtimeWeather();
		realtimeWeather.setTemperature(12);
		realtimeWeather.setHumidity(32);
		realtimeWeather.setLastUpdated(new Date());
		realtimeWeather.setPrecipitation(88);
		realtimeWeather.setStatus("Cloudy");
		realtimeWeather.setWindSpeed(5);
		
		
		realtimeWeather.setLocation(location);
		location.setRealtimeWeather(realtimeWeather);
		
		Mockito.when(locationService.getLocation(Mockito.anyString())).thenReturn(location);
		Mockito.when(realtimeWeatherService.getByLocation(location)).thenReturn(realtimeWeather);
		
		String expectedLocation= location.getCityName() + ", " + location.getRegionName() + ", " + location.getCountryName();
		
		RealtimeWeatherDTO dto = new RealtimeWeatherDTO();
		dto.setLocation(expectedLocation);
		dto.setTemperature(realtimeWeather.getTemperature());
		dto.setHumidity(realtimeWeather.getHumidity());
		dto.setStatus(realtimeWeather.getStatus());
		dto.setWindSpeed(realtimeWeather.getWindSpeed());
		dto.setPrecipitation(realtimeWeather.getPrecipitation());
		dto.setLastUpdated(new Date());
		 
		Mockito.when(modelMapper.map(realtimeWeather, RealtimeWeatherDTO.class)).thenReturn(dto);
	
		mockMvc.perform(get(END_POINT_PATH))
		.andExpect(status().isOk())
		.andExpect(content().contentType("application/hal+json"))
		.andExpect(jsonPath("$.location", is(expectedLocation)))
		.andExpect (jsonPath ("$._links.self.href", is ("http://localhost/v1/realtime")))
		.andExpect (jsonPath ("$._links.hourly_forecast.href", is ("http://localhost/v1/hourly")))
		.andExpect (jsonPath ("$._links.daily_forecast.href", is ("http://localhost/v1/daily")))
		.andExpect (jsonPath ("$._links.full_forecast.href", is ("http://localhost/v1/full")))
		.andDo(print());
	}
	
	@Test
	public void testGetByLocationCodeShouldReturnStatus404NotFound() throws Exception {
		String locationCode = "ADB_IU";
		
		Mockito.when(realtimeWeatherService.getByLocationCode(locationCode)).thenThrow(LocationNotFoundException.class);
		
		String requestURI = END_POINT_PATH + "/" + locationCode;
		
		mockMvc.perform(get(requestURI))
		.andExpect(status().isNotFound())
		.andDo(print());
	}
	
	@Test
	public void testGetByLocationCodeShouldReturnStatus200Ok() throws Exception {
		String locationCode = "NYC_USA";
		
		Location location = new Location();
		location.setCode(locationCode);
		location.setCityName("New York City");
		location.setRegionName("New York");
		location.setCountryName("USA");
		location.setCountryCode("US");
		
		RealtimeWeather realtimeWeather = new RealtimeWeather();
		realtimeWeather.setTemperature(22);
		realtimeWeather.setHumidity(52);
		realtimeWeather.setLastUpdated(new Date());
		realtimeWeather.setPrecipitation(45);
		realtimeWeather.setStatus("Sunny");
		realtimeWeather.setWindSpeed(8);
		
		realtimeWeather.setLocation(location);
		location.setRealtimeWeather(realtimeWeather);
		
		Mockito.when(realtimeWeatherService.getByLocationCode(locationCode)).thenReturn(realtimeWeather);
		
		String expectedLocation= location.getCityName() + ", " + location.getRegionName() + ", " + location.getCountryName();
		
		RealtimeWeatherDTO dto = new RealtimeWeatherDTO();
		dto.setLocation(expectedLocation);
		dto.setTemperature(realtimeWeather.getTemperature());
		dto.setHumidity(realtimeWeather.getHumidity());
		dto.setStatus(realtimeWeather.getStatus());
		dto.setWindSpeed(realtimeWeather.getWindSpeed());
		dto.setPrecipitation(realtimeWeather.getPrecipitation());
		dto.setLastUpdated(new Date());
		
		Mockito.when(modelMapper.map(realtimeWeather, RealtimeWeatherDTO.class)).thenReturn(dto);
		
		String requestURI = END_POINT_PATH + "/" + locationCode;
		
		mockMvc.perform(get(requestURI))
		.andExpect(status().isOk())
		.andExpect(content().contentType("application/hal+json"))
		.andExpect(jsonPath("$.location", is(expectedLocation)))
		.andExpect (jsonPath ("$._links.self.href", is ("http://localhost/v1/realtime/" + locationCode)))
		.andExpect (jsonPath ("$._links.hourly_forecast.href", is ("http://localhost/v1/hourly/" + locationCode)))
		.andExpect (jsonPath ("$._links.daily_forecast.href", is ("http://localhost/v1/daily/" + locationCode)))
		.andExpect (jsonPath ("$._links.full_forecast.href", is ("http://localhost/v1/full/" + locationCode)))
		.andDo(print());
	}
	
	@Test
	public void testUpdatesShouldReturn400BadRequest() throws Exception {
		
		String locationCode = "ADB_IU";
		String requestURI = END_POINT_PATH + "/" + locationCode;
		
		RealtimeWeather realtimeWeather = new RealtimeWeather();
		realtimeWeather.setTemperature(120);
		realtimeWeather.setHumidity(132);
		realtimeWeather.setPrecipitation(188);
		realtimeWeather.setStatus("Sunny");
		realtimeWeather.setWindSpeed(500);
		
		String bodyContent = mapper.writeValueAsString(realtimeWeather);
		
		mockMvc.perform(put(requestURI).contentType("application/json").content(bodyContent))
		       .andExpect(status().isBadRequest())
		       .andDo(print());
	}
	
	
	@Test
	public void testUpdatesShouldReturn404NotFound() throws Exception {
	
		String locationCode = "ADS_IN";
		String requestURI = END_POINT_PATH + "/" + locationCode;
		
		RealtimeWeather realtimeWeather = new RealtimeWeather();
		realtimeWeather.setTemperature(12);
		realtimeWeather.setHumidity(32);
		realtimeWeather.setPrecipitation(88);
		realtimeWeather.setStatus("Sunny");
		realtimeWeather.setWindSpeed(5);
		realtimeWeather.setLastUpdated(new Date());
		realtimeWeather.setLocationCode(locationCode);
		
		LocationNotFoundException exception = new LocationNotFoundException(locationCode);
		Mockito.when(realtimeWeatherService.update(locationCode, realtimeWeather)).thenThrow(exception);
		
		String bodyContent = mapper.writeValueAsString(realtimeWeather);
		
		mockMvc.perform(put(requestURI).contentType("application/json").content(bodyContent))
		       .andExpect(status().isNotFound())
		       .andExpect(jsonPath("$.errors[0]", is (exception.getMessage())))
		       .andDo(print());
	}
	
	
	@Test
	public void testUpdatesShouldReturn200Ok() throws Exception {
		
		String locationCode = "NYC_USA";
		String requestURI = END_POINT_PATH + "/" + locationCode;
		
		RealtimeWeather realtimeWeather = new RealtimeWeather();
		realtimeWeather.setTemperature(12);
		realtimeWeather.setHumidity(32);
		realtimeWeather.setPrecipitation(88);
		realtimeWeather.setStatus("Sunny");
		realtimeWeather.setWindSpeed(5);
		realtimeWeather.setLastUpdated (new Date ());

		RealtimeWeatherDTO dto = new RealtimeWeatherDTO();
		dto.setTemperature(12);
		dto.setHumidity(32);
		dto.setStatus("Sunny");
		dto.setWindSpeed(5);
		dto.setPrecipitation(88);

		Location location = new Location();
		location.setCode(locationCode);
		location.setCityName("New York City");
		location.setRegionName("New York");
		location.setCountryName("USA");
		location.setCountryCode("US");
		
		realtimeWeather.setLocation(location);
		location.setRealtimeWeather(realtimeWeather);

		Mockito.when(modelMapper.map(any(RealtimeWeatherDTO.class), eq(RealtimeWeather.class))).thenReturn(new RealtimeWeather());
		Mockito.when(realtimeWeatherService.update(eq(locationCode), any(RealtimeWeather.class))).thenReturn(realtimeWeather);

		String bodyContent = mapper.writeValueAsString(dto);
		String expectedLocation= location.getCityName() + ", " + location.getRegionName() + ", " + location.getCountryName();

		Mockito.when(modelMapper.map(any(RealtimeWeather.class), eq(RealtimeWeatherDTO.class))).thenReturn(dto);
		dto.setLocation (expectedLocation);

		mockMvc.perform(put(requestURI).contentType(REQUEST_CONTENT_TYPE).content(bodyContent))
		        .andExpect(status().isOk())
				.andExpect(jsonPath("$.location", is(expectedLocation)))
				.andExpect(content().contentType(RESPONSE_CONTENT_TYPE))
				.andExpect (jsonPath ("$._links.self.href", is ("http://localhost/v1/realtime/" + locationCode)))
				.andExpect (jsonPath ("$._links.hourly_forecast.href", is ("http://localhost/v1/hourly/" + locationCode)))
				.andExpect (jsonPath ("$._links.daily_forecast.href", is ("http://localhost/v1/daily/" + locationCode)))
				.andExpect (jsonPath ("$._links.full_forecast.href", is ("http://localhost/v1/full/" + locationCode)))
		       .andDo(print());
	}
}
