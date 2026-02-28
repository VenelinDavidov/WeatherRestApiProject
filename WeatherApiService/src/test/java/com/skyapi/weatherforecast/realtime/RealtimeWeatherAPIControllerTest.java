package com.skyapi.weatherforecast.realtime;

import static org.hamcrest.CoreMatchers.is;
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

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	ObjectMapper mapper;
	
	@MockBean 
	RealtimeWeatherService realtimeWetherService;
	
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
		Mockito.when(locationService.getLocation(Mockito.anyString())).thenReturn(location);
		Mockito.when(realtimeWetherService.getByLocation(location)).thenThrow(LocationNotFoundException.class);
		
		mockMvc.perform(get(END_POINT_PATH))
		.andExpect(status().isNotFound())
		.andDo(print());
	}
	
	
	
	@Test
	public void testGetShouldReturn200Ok() throws Exception {
		
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
		Mockito.when(realtimeWetherService.getByLocation(location)).thenReturn(realtimeWeather);
		
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
		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.location", is(expectedLocation)))
		.andDo(print());
	}
	
	@Test
	public void testGetByLocationCodeShouldReturnStatus404NotFound() throws Exception {
		String locationCode = "ADB_IU";
		
		Mockito.when(realtimeWetherService.getByLocationCode(locationCode)).thenThrow(LocationNotFoundException.class);
		
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
		
		Mockito.when(realtimeWetherService.getByLocationCode(locationCode)).thenReturn(realtimeWeather);
		
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
		.andExpect(content().contentType("application/json"))
		.andExpect(jsonPath("$.location", is(expectedLocation)))
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
		
		Mockito.when(realtimeWetherService.update(locationCode, realtimeWeather)).thenThrow(LocationNotFoundException.class);
		
		String bodyContent = mapper.writeValueAsString(realtimeWeather);
		
		mockMvc.perform(put(requestURI).contentType("application/json").content(bodyContent))
		       .andExpect(status().isNotFound())
		       .andDo(print());
	}
	
	
	@Test
	public void testUpdatesShouldReturn200Ok() throws Exception {
		
		String locationCode = "NYC_USA";
		String requestURI = END_POINT_PATH + "/" + locationCode;
		
		Location location = new Location();
		location.setCode(locationCode);
		location.setCityName("New York City");
		location.setRegionName("New York");
		location.setCountryName("USA");
		location.setCountryCode("US");
		
		RealtimeWeather realtimeWeather = new RealtimeWeather();
		realtimeWeather.setTemperature(12);
		realtimeWeather.setHumidity(32);
		realtimeWeather.setPrecipitation(88);
		realtimeWeather.setStatus("Sunny");
		realtimeWeather.setWindSpeed(5);
		
		realtimeWeather.setLocation(location);
		location.setRealtimeWeather(realtimeWeather);
		
		Mockito.when(realtimeWetherService.update(locationCode, realtimeWeather)).thenReturn(realtimeWeather);
		
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
		String bodyContent = mapper.writeValueAsString(realtimeWeather);
		
		mockMvc.perform(put(requestURI).contentType("application/json").content(bodyContent))
		       .andExpect(status().isOk())
		       .andExpect(jsonPath("$.location", is(expectedLocation)))
		       .andDo(print());
	}
		
}
