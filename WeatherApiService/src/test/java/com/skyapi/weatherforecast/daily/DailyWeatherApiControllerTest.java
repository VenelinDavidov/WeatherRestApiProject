package com.skyapi.weatherforecast.daily;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
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
