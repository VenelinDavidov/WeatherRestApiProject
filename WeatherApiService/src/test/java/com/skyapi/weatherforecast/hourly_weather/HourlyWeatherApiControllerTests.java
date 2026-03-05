package com.skyapi.weatherforecast.hourly_weather;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import com.skyapi.weatherforecast.common.HourlyWeather;
import com.skyapi.weatherforecast.common.Location;
import com.skyapi.weatherforecast.hourly_weather.service.HourlyWeatherService;
import com.skyapi.weatherforecast.hourly_weather.web.HourlyWeatherAPIController;
import com.skyapi.weatherforecast.location.exceptions.GeoLocationException;
import com.skyapi.weatherforecast.location.service.GeoLocationService;

@WebMvcTest(HourlyWeatherAPIController.class)
public class HourlyWeatherApiControllerTests {

	private static final String X_CURRENT_HOUR = "X-Current-Hour";

	private static final String END_POINT_PATH = "/v1/hourly";
	
	@Autowired private MockMvc mockMvc;
	@MockBean HourlyWeatherService hourlyWeatherService;
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
}
