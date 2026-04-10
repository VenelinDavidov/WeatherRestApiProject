package com.skyapi.weatherforecast.full.web.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.skyapi.weatherforecast.daily.web.dto.DailyWeatherDto;
import com.skyapi.weatherforecast.hourly_weather.web.dto.HourlyWeatherDto;
import com.skyapi.weatherforecast.realtime.web.dto.RealtimeWeatherDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;

public class FullWeatherDTO {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String location;

    @JsonProperty("realtime_weather")
    @Valid  @NotNull
    private RealtimeWeatherDTO realtimeWeather = new RealtimeWeatherDTO ();

    @JsonProperty("hourly_forecast")
    @Valid
    private List <HourlyWeatherDto> listHourlyWeather = new ArrayList <> ();

    @JsonProperty("daily_forecast")
    @Valid
    private List<DailyWeatherDto> listDailyWeather = new ArrayList <> ();


    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public RealtimeWeatherDTO getRealtimeWeather() {
        return realtimeWeather;
    }

    public void setRealtimeWeather(RealtimeWeatherDTO realtimeWeather) {
        this.realtimeWeather = realtimeWeather;
    }

    public List <HourlyWeatherDto> getListHourlyWeather() {
        return listHourlyWeather;
    }

    public void setListHourlyWeather(List <HourlyWeatherDto> listHourlyWeather) {
        this.listHourlyWeather = listHourlyWeather;
    }

    public List <DailyWeatherDto> getListDailyWeather() {
        return listDailyWeather;
    }

    public void setListDailyWeather(List <DailyWeatherDto> listDailyWeather) {
        this.listDailyWeather = listDailyWeather;
    }
}
