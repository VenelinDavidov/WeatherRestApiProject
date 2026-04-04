package com.skyapi.weatherforecast.daily;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import com.skyapi.weatherforecast.common.DailyWeather;
import com.skyapi.weatherforecast.common.Location;
import com.skyapi.weatherforecast.daily.service.DailyWeatherService;
import com.skyapi.weatherforecast.daily.web.DailyWeatherController;
import com.skyapi.weatherforecast.daily.web.dto.DailyWeatherDto;
import com.skyapi.weatherforecast.location.exceptions.GeoLocationException;
import com.skyapi.weatherforecast.location.exceptions.LocationNotFoundException;
import com.skyapi.weatherforecast.location.service.GeoLocationService;

@WebMvcTest(DailyWeatherController.class)
public class DailyWeatherApiControllerTest {

	private static final String END_POINT_PATH = "/v1/daily";

	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private DailyWeatherService dailyWeatherService;
	@MockBean
	private GeoLocationService locationService;
	@Autowired
	private ObjectMapper objectMapper;
	
	@MockBean
	private ModelMapper modelMapper;

	@Test
	public void testGetByIPShouldReturn400BadRequestBecauseGeoLocationException() throws Exception {
		GeoLocationException ex = new GeoLocationException("Erro Geolocation!");
		
		Mockito.when(locationService.getLocation(Mockito.anyString())).thenThrow(ex);
		
		mockMvc.perform(get(END_POINT_PATH))
		       .andExpect(status().isBadRequest())
		       .andExpect(jsonPath("$.errors[0]", is(ex.getMessage())))
		       .andDo(print());
		
	}
	
	@Test
	public void testShouldReturn404NotFound() throws Exception {
		Location location = new Location().code("DELHI_IN");
		Mockito.when(locationService.getLocation(Mockito.anyString())).thenReturn(location);
		
		LocationNotFoundException ex = new LocationNotFoundException(location.getCode());
		Mockito.when(dailyWeatherService.getByLocation(location)).thenThrow(ex);
		
		mockMvc.perform(get(END_POINT_PATH))
		       .andExpect(status().isNoContent())
		       .andDo(print());
	}
	
	
	@Test
	public void testByCodeGetShouldReturn404NotFound() throws Exception {
		
		String locationCode = "LUCA_US";
		String requestURI = END_POINT_PATH + "/" + locationCode;
		
		LocationNotFoundException ex = new LocationNotFoundException(locationCode);	
		when(dailyWeatherService.getByLocationCode(locationCode)).thenThrow(ex);
		
		mockMvc.perform(get(requestURI))
		    .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.errors[0]", is(ex.getMessage())))
            .andDo(print());
	}
	
	
	@Test
	public void testGetByCodeShouldReturn204NoContent() throws Exception {
		String locationCode = "LUCA_US";
		String requestURI = END_POINT_PATH + "/" + locationCode;
		
		when(dailyWeatherService.getByLocationCode(locationCode)).thenReturn(new ArrayList<>());
		
		mockMvc.perform(get(requestURI))
		.andExpect(status().isNoContent())
        .andDo(print());
	}

	@Test
	public void testShouldReturn400BadRequestBecauseNoData() throws Exception {
		String requestURI = END_POINT_PATH + "/NYC_USA";

		List <DailyWeatherDto> listDto = Collections.emptyList ();
		String requestBody = objectMapper.writeValueAsString (listDto);

		mockMvc.perform (put (requestURI).contentType(MediaType.APPLICATION_JSON).content(requestBody))
				.andExpect (status ().isBadRequest ())
				.andExpect (jsonPath ("$.errors[0]", is("Daily forecast data cannot be empty!")))
				.andDo (print ());
	}

	@Test
	public void testUpdatesShouldReturn400BadRequestBecauseInvalidData() throws Exception {
		String requestURI = END_POINT_PATH + "/NYC_USA";

		DailyWeatherDto dto1 = new DailyWeatherDto ()
				.dayOfMonth (40)
				.month (7)
				.minTemp (23)
				.maxTemp (34)
				.precipitation (58)
				.status ("Clear");

		DailyWeatherDto dto2 = new DailyWeatherDto ()
				.dayOfMonth (20)
				.month (7)
				.minTemp (23)
				.maxTemp (42)
				.precipitation (58)
				.status ("Clear");

		List <DailyWeatherDto> listDto = List.of (dto1, dto2);
		String requestBody = objectMapper.writeValueAsString (listDto);

		mockMvc.perform (put (requestURI).contentType (MediaType.APPLICATION_JSON).content (requestBody))
				.andExpect (status ().isBadRequest ())
				.andExpect (jsonPath ("$.errors[0]", containsString("Day of month must be between 1-31")))
				.andDo (print ());

	}

	@Test
	public void testUpdateShouldReturn404NotFound() throws Exception {
		String locationCode = "NYC_USA";
		String requestURI = END_POINT_PATH + "/" + locationCode;

		DailyWeatherDto dto = new DailyWeatherDto ()
				.dayOfMonth (21)
				.month (7)
				.minTemp (23)
				.maxTemp (32)
				.precipitation (52)
				.status ("Clear");

		List <DailyWeatherDto> listDto = List.of (dto);
		String requestBody = objectMapper.writeValueAsString (listDto);

		LocationNotFoundException ex = new LocationNotFoundException (locationCode);
		when (dailyWeatherService.updateByLocationCode (Mockito.eq (locationCode), Mockito.anyList ())).thenThrow (ex);

		mockMvc.perform (put (requestURI).contentType (MediaType.APPLICATION_JSON).content (requestBody))
				.andExpect (status ().isNotFound ())
				.andDo (print ());
	}

	@Test
	public void testUpdatedShouldReturn200Ok() throws Exception {
		String locationCode = "NYC_USA";
		String requestURI = END_POINT_PATH + "/" + locationCode;

		DailyWeatherDto dto1 = new DailyWeatherDto ()
				.dayOfMonth (17)
				.month (7)
				.minTemp (25)
				.maxTemp (35)
				.precipitation (52)
				.status ("Sunny");

		DailyWeatherDto dto2 = new DailyWeatherDto ()
				.dayOfMonth (18)
				.month (7)
				.minTemp (26)
				.maxTemp (34)
				.precipitation (60)
				.status ("Clear");

		Location location = new Location ();
		location.setCode("NYC_USA");
		location.setCityName("New York City");
		location.setRegionName("New York");
		location.setCountryCode("US");
		location.setCountryName("USA");

		DailyWeather forecast1 = new DailyWeather ()
				.location (location)
				.dayOfMonth (17)
				.month (7)
				.minTemp (25)
				.maxTemp (35)
				.precipitation (52)
				.status ("Sunny");

		DailyWeather forecast2 = new DailyWeather ()
				.location (location)
				.dayOfMonth (18)
				.month (7)
				.minTemp (26)
				.maxTemp (34)
				.precipitation (60)
				.status ("Clear");

		var listDto = List.of (dto1, dto2);
		var dailyForecast = List.of (forecast1, forecast2);

		String requestBody = objectMapper.writeValueAsString (listDto);
		when (dailyWeatherService.updateByLocationCode (Mockito.eq (locationCode),Mockito.anyList ())).thenReturn (dailyForecast);

		mockMvc.perform (put (requestURI).contentType (MediaType.APPLICATION_JSON).content (requestBody))
				.andExpect (status ().isOk ())
				.andExpect (jsonPath ("$.location", is (location.toString ())))
				.andExpect (jsonPath ("$.daily_forecast[0].day_of_month", is (17)))
				.andDo (print());
	}
	
	
	@Test
	public void testGetByIpShouldReturn200Ok() throws Exception  {
		
		Location location = new Location();
		location.setCode("NYC_USA");
		location.setCityName("New York City");
		location.setRegionName("New York");
		location.setCountryCode("US");
		location.setCountryName("USA");
		
		
		DailyWeather forecast1 = new DailyWeather()
				.location(location)
				.dayOfMonth(25)
				.month(6)
				.minTemp(10)
				.maxTemp(25)
				.precipitation(45)
				.status("Sunny");
		
		DailyWeather forecast2 = new DailyWeather()
				.location(location)
				.dayOfMonth(26)
				.month(6)
				.minTemp(12)
				.maxTemp(32)
				.precipitation(25)
				.status("Clearly");
		
		Mockito.when(locationService.getLocation(Mockito.anyString())).thenReturn(location);
		Mockito.when(dailyWeatherService.getByLocation(location)).thenReturn(List.of(forecast1,forecast2));
		
		DailyWeatherDto dto1 = new DailyWeatherDto();
		dto1.setDayOfMonth(26);
		dto1.setMonth(6);
		dto1.setMinTemp(20);
		dto1.setMaxTemp(32);
		dto1.setPrecipitation(78);
		dto1.setStatus("Rain");
	
		
		DailyWeatherDto dto2 = new DailyWeatherDto();
		dto1.setDayOfMonth(25);
		dto1.setMonth(6);
		dto1.setMinTemp(23);
		dto1.setMaxTemp(35);
		dto1.setPrecipitation(68);
		dto1.setStatus("Sunny");
		
		
		
		Mockito.when(modelMapper.map(forecast1, DailyWeatherDto.class)).thenReturn(dto1);
		Mockito.when(modelMapper.map(forecast2, DailyWeatherDto.class)).thenReturn(dto2);
		
		String expectedLocation = location.toString();
		
		mockMvc.perform(get(END_POINT_PATH))
		             .andExpect(status().isOk())
		             .andExpect(content().contentType("application/json"))
		             .andExpect(jsonPath("$.location", is(expectedLocation)))
		             .andExpect(jsonPath("$.daily_forecast[0].day_of_month", is(25)))
		             .andDo(print());
	}
}
