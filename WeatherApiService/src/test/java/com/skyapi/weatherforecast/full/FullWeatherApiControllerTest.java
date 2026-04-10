package com.skyapi.weatherforecast.full;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skyapi.weatherforecast.common.DailyWeather;
import com.skyapi.weatherforecast.common.HourlyWeather;
import com.skyapi.weatherforecast.common.Location;
import com.skyapi.weatherforecast.common.RealtimeWeather;

import com.skyapi.weatherforecast.daily.web.dto.DailyWeatherDto;
import com.skyapi.weatherforecast.full.service.FullWeatherService;
import com.skyapi.weatherforecast.full.web.FullWeatherApiController;

import com.skyapi.weatherforecast.full.web.dto.FullWeatherDTO;
import com.skyapi.weatherforecast.hourly_weather.web.dto.HourlyWeatherDto;
import com.skyapi.weatherforecast.location.exceptions.GeoLocationException;
import com.skyapi.weatherforecast.location.exceptions.LocationNotFoundException;
import com.skyapi.weatherforecast.location.service.GeoLocationService;
import com.skyapi.weatherforecast.realtime.web.dto.RealtimeWeatherDTO;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;
import java.util.List;


import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FullWeatherApiController.class)
public class FullWeatherApiControllerTest {

    private static final String END_POINT_PATH = "/v1/full";

    @Autowired private MockMvc mockMvc;
    @MockBean private FullWeatherService fullWeatherService;
    @MockBean private GeoLocationService locationService;
    @Autowired private ObjectMapper objectMapper;
    @MockBean ModelMapper modelMapper;


    @Test
    public void testGetByIPShouldReturn400BadRequestBecauseGeoLocationException() throws Exception {

        GeoLocationException ex = new GeoLocationException("Error Geolocation!");
        when(locationService.getLocation(Mockito.anyString())).thenThrow(ex);

        mockMvc.perform(get(END_POINT_PATH))
                .andExpect(status().isBadRequest ())
                .andExpect(jsonPath("$.errors[0]", is (ex.getMessage())))
                .andDo(print());

    }

    @Test
    public void testShouldReturn404NotFound() throws Exception {
        Location location = new Location().code("DELHI_IN");
        when(locationService.getLocation(Mockito.anyString())).thenReturn(location);

        LocationNotFoundException ex = new LocationNotFoundException(location.getCode());
        when(fullWeatherService.getByLocation(location)).thenThrow(ex);

        mockMvc.perform(get(END_POINT_PATH))
                .andExpect(status().isNotFound ())
                .andExpect (jsonPath ("$.errors[0]", is (ex.getMessage ())))
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


        RealtimeWeather realtimeWeather = new RealtimeWeather();
        realtimeWeather.setTemperature(12);
        realtimeWeather.setHumidity(32);
        realtimeWeather.setLastUpdated(new Date ());
        realtimeWeather.setPrecipitation(88);
        realtimeWeather.setStatus("Cloudy");
        realtimeWeather.setWindSpeed(5);

        location.setRealtimeWeather (realtimeWeather);

        DailyWeather dailyForecast1 = new DailyWeather()
                .location(location)
                .dayOfMonth(25)
                .month(6)
                .minTemp(10)
                .maxTemp(25)
                .precipitation(45)
                .status("Sunny");

        DailyWeather dailyForecast2 = new DailyWeather()
                .location(location)
                .dayOfMonth(26)
                .month(6)
                .minTemp(12)
                .maxTemp(32)
                .precipitation(25)
                .status("Clearly");

        location.setListDailyWeather (List.of (dailyForecast1,dailyForecast2));

        HourlyWeather hourlyForecast1 = new HourlyWeather()
                .id(location, 10)
                .temperature(20)
                .precipitation(30)
                .status("Cloudy");


        HourlyWeather hourlyForecast2 = new HourlyWeather()
                .id(location, 11)
                .temperature(25)
                .precipitation(45)
                .status("Sunny");

        location.setListHourlyWeather (List.of (hourlyForecast1,hourlyForecast2));


        when(locationService.getLocation(Mockito.anyString())).thenReturn(location);
        when(fullWeatherService.getByLocation(Mockito.any(Location.class))).thenReturn(location);


        String expectedLocation = location.toString ();

        mockMvc.perform(get(END_POINT_PATH))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.location.city_name", is("New York City")))
                .andExpect(jsonPath("$.realtime_weather.temperature", is(12)))
                .andExpect(jsonPath("$.hourly_forecast[0].hour_of_day", is(10)))
                .andDo(print());
    }


    @Test
    public void testUpdateShouldReturn400BadRequestBecauseNoHourlyWeather() throws Exception {

        String locationCode = "NYC_USA";
        String requestURI = END_POINT_PATH + "/" + locationCode;

        FullWeatherDTO dtoFullWeather = new FullWeatherDTO ();
        String requestBody = objectMapper.writeValueAsString (dtoFullWeather);


        mockMvc.perform(put (requestURI).contentType ("application/json").content (requestBody))
                .andExpect(status().isBadRequest ())
                .andExpect(jsonPath("$.errors[0]", is ("Hourly weather data cannot be empty!")))
                .andDo(print());

    }

    @Test
    public void testUpdateShouldReturn400BadRequestBecauseNoDailyWeather() throws Exception {

        String locationCode = "NYC_USA";
        String requestURI = END_POINT_PATH + "/" + locationCode;

        FullWeatherDTO dtoFullWeather = new FullWeatherDTO ();

        HourlyWeatherDto hourlyForecast1 = new HourlyWeatherDto ()
                .hourOfDay (10)
                .temperature(20)
                .precipitation(30)
                .status("Cloudy");

        dtoFullWeather.getListHourlyWeather ().add (hourlyForecast1);

        String requestBody = objectMapper.writeValueAsString (dtoFullWeather);

        mockMvc.perform(put (requestURI).contentType ("application/json").content (requestBody))
                .andExpect(status().isBadRequest ())
                .andExpect(jsonPath("$.errors[0]", is ("Daily weather data cannot be empty!")))
                .andDo(print());

    }

    @Test
    public void testUpdateShouldReturn400BadRequestBecauseInvalidRealtimeWeather() throws Exception {

        String locationCode = "NYC_USA";
        String requestURI = END_POINT_PATH + "/" + locationCode;

        FullWeatherDTO dtoFullWeather = new FullWeatherDTO ();

        HourlyWeatherDto hourlyForecast1 = new HourlyWeatherDto ()
                .hourOfDay (10)
                .temperature(20)
                .precipitation(30)
                .status("Cloudy");

        dtoFullWeather.getListHourlyWeather ().add (hourlyForecast1);

        DailyWeatherDto dailyForecast1 = new DailyWeatherDto ()
                .dayOfMonth(17)
                .month(7)
                .minTemp(25)
                .maxTemp(34)
                .precipitation(30)
                .status("Sunny");

        dtoFullWeather.getListDailyWeather ().add (dailyForecast1);

        RealtimeWeatherDTO realtimeWeatherDTO = new RealtimeWeatherDTO ();
        realtimeWeatherDTO.setTemperature(122);
        realtimeWeatherDTO.setHumidity(32);
        realtimeWeatherDTO.setLastUpdated(new Date ());
        realtimeWeatherDTO.setPrecipitation(88);
        realtimeWeatherDTO.setStatus("Cloudy");
        realtimeWeatherDTO.setWindSpeed(5);

        dtoFullWeather.setRealtimeWeather (realtimeWeatherDTO);

        String requestBody = objectMapper.writeValueAsString (dtoFullWeather);

        mockMvc.perform(put (requestURI).contentType ("application/json").content (requestBody))
                .andExpect(status().isBadRequest ())
                .andExpect(jsonPath("$.errors[0]", containsString ("Temperature must be in the range!")))
                .andDo(print());

    }
}
