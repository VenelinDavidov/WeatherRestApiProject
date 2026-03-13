package com.skyapi.weatherforecast.hourly_weather;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skyapi.weatherforecast.common.HourlyWeather;
import com.skyapi.weatherforecast.common.Location;
import com.skyapi.weatherforecast.hourly_weather.service.HourlyWeatherService;
import com.skyapi.weatherforecast.hourly_weather.web.HourlyWeatherAPIController;
import com.skyapi.weatherforecast.hourly_weather.web.dto.HourlyWeatherDto;
import com.skyapi.weatherforecast.location.exceptions.GeoLocationException;
import com.skyapi.weatherforecast.location.exceptions.LocationNotFoundException;
import com.skyapi.weatherforecast.location.service.GeoLocationService;

@WebMvcTest(HourlyWeatherAPIController.class)
public class HourlyWeatherApiControllerTests {

	private static final String X_CURRENT_HOUR = "X-Current-Hour";

	private static final String END_POINT_PATH = "/v1/hourly";
	
	@Autowired private MockMvc mockMvc;
	@MockBean HourlyWeatherService hourlyWeatherService;
	@Autowired ObjectMapper mapper;
	@MockBean GeoLocationService locationService;
	
	
	@Test
	public void testGetByIpShouldReturn400BadRequestBecauseNoHeaderXCurrentHour() throws Exception {
		
		mockMvc.perform(get(END_POINT_PATH))
		       .andExpect(status().isBadRequest())
		       .andDo(print());		
	}
	
	@Test
	public void testGetByIpShouldReturn400BadRequestBecauseGeoLocationExceptionLocation() throws Exception {
		
		Mockito.when(locationService.getLocation(Mockito.anyString())).thenThrow(GeoLocationException.class);
		
		mockMvc.perform(get(END_POINT_PATH).header(X_CURRENT_HOUR, "9"))
		       .andExpect(status().isBadRequest())
		       .andDo(print());		
	}
	
	@Test
	public void testGetByIpShouldReturn204NoContent() throws Exception {
		
		int currentHour = 9;
		Location location = new Location().code("DELHI_IN");
		
		Mockito.when(locationService.getLocation(Mockito.anyString())).thenReturn(location);
		Mockito.when(hourlyWeatherService.getByLocation(location, currentHour)).thenReturn(new ArrayList<>());
		
		mockMvc.perform(get(END_POINT_PATH).header(X_CURRENT_HOUR, String.valueOf(currentHour)))
		       .andExpect(status().isNoContent())
		       .andDo(print());		
	}
	
	@Test
	public void testGetByIpShouldReturn200OK() throws Exception {
		
		int currentHour = 9;
		
		Location location = new Location();
		location.setCode("NYC_USA");
		location.setCityName("New York City");
		location.setRegionName("New York");
		location.setCountryCode("US");
		location.setCountryName("USA");
		location.setEnabled(true);
		
		HourlyWeather forecast1 = new HourlyWeather()
				.id(location, 10)
				.temperature(20)
				.precipitation(30)
				.status("Cloudy");
		
		
		HourlyWeather forecast2 = new HourlyWeather()
				.id(location, 11)
				.temperature(25)
				.precipitation(45)
				.status("Sunny");
		
		
		Mockito.when(locationService.getLocation(Mockito.anyString())).thenReturn(location);
		Mockito.when(hourlyWeatherService.getByLocation(location, currentHour)).thenReturn(List.of(forecast1, forecast2));
		
		String expectedLocation = location.toString();
		
		mockMvc.perform(get(END_POINT_PATH).header(X_CURRENT_HOUR, String.valueOf(currentHour)))
		       .andExpect(status().isOk())
		       .andExpect(jsonPath("$.location", is(expectedLocation)))
		       .andExpect(jsonPath("$.hourly_forecast[0].hour_of_day", is(10)))		      
		       .andDo(print());		
	}
	
	@Test
	public void testGetByCodeShouldReturn400BadRequest() throws Exception {
		String locationCode = "NYC_USA";
		String requestUrl = END_POINT_PATH + "/" + locationCode;
		
		mockMvc.perform(get(requestUrl))
		.andExpect(status().isBadRequest())
		.andDo(print());
		
	}
	
	@Test
	public void testGetByCodeShouldReturn404NotFound() throws Exception {
		String locationCode = "NYC_USA";
		String requestUrl = END_POINT_PATH + "/" + locationCode;
		int currentHour= 9;
		
		Mockito.when(hourlyWeatherService.getByLocationCode(locationCode, currentHour)).thenThrow(LocationNotFoundException.class);

		mockMvc.perform(get(requestUrl).header("X-Current-Hour", String.valueOf(currentHour)))
		.andExpect(status().isNotFound())
		.andDo(print());
		
	}
	
	@Test
	public void testGetByCodeShouldReturn204NoContent() throws Exception {
		String locationCode = "NYC_USA";
		String requestUrl = END_POINT_PATH + "/" + locationCode;
		int currentHour= 9;
		
		Mockito.when(hourlyWeatherService.getByLocationCode(locationCode, currentHour)).thenReturn(Collections.emptyList());

		mockMvc.perform(get(requestUrl).header("X-Current-Hour", String.valueOf(currentHour)))
		.andExpect(status().isNoContent())
		.andDo(print());
		
	}
	
	@Test
	public void testGetByCodeShouldReturn200Ok() throws Exception {
		String locationCode = "NYC_USA";
		String requestUrl = END_POINT_PATH + "/" + locationCode;
		int currentHour= 9;
		
		Location location = new Location();
		location.setCode(locationCode);
		location.setCityName("New York City");
		location.setRegionName("New York");
		location.setCountryName("USA");
		location.setCountryCode("US");
		
		HourlyWeather forecast1 = new HourlyWeather()
				.id(location, 8)
				.temperature(32)
				.precipitation(40)
				.status("Sunny");
		
		
		HourlyWeather forecast2 = new HourlyWeather()
				.id(location, 7)
				.temperature(30)
				.precipitation(45)
				.status("Sunny");
		
		var hourlyWeather = List.of(forecast1,forecast2);

		Mockito.when(hourlyWeatherService.getByLocationCode(locationCode, currentHour)).thenReturn(hourlyWeather);
		
		mockMvc.perform(get(requestUrl).header("X-Current-Hour", String.valueOf(currentHour)))
		.andExpect(status().isOk())
		.andExpect(content().contentType("application/json"))
		.andExpect(jsonPath("$.location", is(location.toString())))
	    .andExpect(jsonPath("$.hourly_forecast[0].hour_of_day", is(8)))
		.andDo(print());
	}
	
	
	
	@Test
	public void testUpdateShouldReturn400BadRequestBecauseNoData() throws Exception {
		
		String requestURL = END_POINT_PATH + "/NYC_USA";
		
		List<HourlyWeatherDto> listDto = Collections.emptyList();
		String requestBody = mapper.writeValueAsString(listDto);
		
		mockMvc.perform(put(requestURL).contentType(MediaType.APPLICATION_JSON).content(requestBody))
		.andExpect(status().isBadRequest())
		.andExpect(jsonPath("$.errors[0]", is("Hourly forecast data cannot be empty!")))
		.andDo(print());
	}
	
	
	
	@Test
	public void testUpdateShouldReturn400BadRequestBecauseInvalidData() throws Exception {
		
		String requestURL = END_POINT_PATH + "/NYC_USA";
		
		HourlyWeatherDto dto1 = new HourlyWeatherDto()
				.hourOfDay(102)
				.temperature(233)
				.precipitation(453)
				.status("Sunny");
		  
		HourlyWeatherDto dto2 = new HourlyWeatherDto()
				.hourOfDay(112)
				.temperature(25523)
				.precipitation(56)
				.status("");
		
		List<HourlyWeatherDto> listDto = List.of(dto1,dto2);
		String requestBody = mapper.writeValueAsString(listDto);
		
		mockMvc.perform(put(requestURL).contentType(MediaType.APPLICATION_JSON).content(requestBody))
		.andExpect(status().isBadRequest())
		.andDo(print());
	}
	
	@Test
	public void testUpdateShouldReturn404NotFound() throws Exception {
		String locationCode = "NYC_USA";
		String requestURL = END_POINT_PATH + "/NYC_USA";
		
		HourlyWeatherDto dto1 = new HourlyWeatherDto()
				.hourOfDay(10)
				.temperature(23)
				.precipitation(70)
				.status("Sunny");
		  
		
		List<HourlyWeatherDto> listDto = List.of(dto1);
		String requestBody = mapper.writeValueAsString(listDto);
		
		Mockito.when(hourlyWeatherService.updateByLocationCode(Mockito.eq(locationCode) , Mockito.anyList()))
		                                                          .thenThrow(LocationNotFoundException.class);
		
		mockMvc.perform(put(requestURL).contentType(MediaType.APPLICATION_JSON).content(requestBody))
		.andExpect(status().isNotFound())
		.andDo(print());
	}
	 
	
	
	
	@Test
	public void testUpdateShouldReturn200Ok() throws Exception {
		String locationCode = "NYC_USA";
		String requestURL = END_POINT_PATH + "/NYC_USA";
		
		HourlyWeatherDto dto1 = new HourlyWeatherDto()
				.hourOfDay(10)
				.temperature(23)
				.precipitation(70)
				.status("Sunny");
		  
		HourlyWeatherDto dto2 = new HourlyWeatherDto()
				.hourOfDay(11)
				.temperature(25)
				.precipitation(56)
				.status("Cloudy");
		
		Location location = new Location();
		location.setCode(locationCode);
		location.setCityName("New York City");
		location.setRegionName("New York");
		location.setCountryName("USA");
		location.setCountryCode("US");
		
		HourlyWeather forecast1 = new HourlyWeather()
				.id(location, 7)
				.temperature(32)
				.precipitation(40)
				.status("Sunny");
		
		
		HourlyWeather forecast2 = new HourlyWeather()
				.id(location, 6)
				.temperature(30)
				.precipitation(45)
				.status("Sunny");
		
		List<HourlyWeatherDto> listDto = List.of(dto1, dto2);
		
		var hourlyForecast = List.of(forecast1, forecast2);
		String requestBody = mapper.writeValueAsString(listDto);
		
		Mockito.when(hourlyWeatherService.updateByLocationCode(Mockito.eq(locationCode) , Mockito.anyList()))
		                                                          .thenReturn(hourlyForecast);
		
		mockMvc.perform(put(requestURL).contentType(MediaType.APPLICATION_JSON).content(requestBody))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.location", is(location.toString())))
		.andExpect(jsonPath("$.hourly_forecast[0].hour_of_day", is(7)))
		.andDo(print());
	}
}
