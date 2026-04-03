package com.skyapi.weatherforecast.daily.web.dto;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DailyWeatherListDto {

	private String location;
	
	@JsonProperty("daily_forecast")
	private List<DailyWeatherDto> dailyForecast = new ArrayList<>();

	
	
	
	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public List<DailyWeatherDto> getDailyForecast() {
		return dailyForecast;
	}

	public void setDailyForecast(List<DailyWeatherDto> dailyForecast) {
		this.dailyForecast = dailyForecast;
	}
	
	
	
	
	public void addDailyWeatherDto(DailyWeatherDto dto) {
		this.dailyForecast.add(dto);
	}
}
