package com.skyapi.weatherforecast.hourly_weather.web.dto;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.skyapi.weatherforecast.common.HourlyWeather;
import com.skyapi.weatherforecast.common.Location;

import jakarta.validation.constraints.NotBlank;

@JsonPropertyOrder({"hourOfDay", "temperature", "precipitation", "status"})
public class HourlyWeatherDto {

	@JsonProperty("hour_of_day")
	private int hourOfDay;
	
	@Range(min = -50, max = 50,message = "Temperature must be in the range -50 to 50 Celsius!")
	private int temperature;
	
	@Range(min = 0, max = 100,message = "Percipitation must be in the range 0 to 100 percentage!")
	private int precipitation;
	
	@NotBlank(message = "Status must not be empty!")
	@Length(min =3, max =60, message = "Status must be in between 3-50 characters!	")
	private String status;

	
	
	

	public int getHourOfDay() {
		return hourOfDay;
	}

	public void setHourOfDay(int hourOfDay) {
		this.hourOfDay = hourOfDay;
	}

	public int getTemperature() {
		return temperature;
	}

	public void setTemperature(int temperature) {
		this.temperature = temperature;
	}

	public int getPrecipitation() {
		return precipitation;
	}

	public void setPrecipitation(int precipitation) {
		this.precipitation = precipitation;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	
	
	
	public HourlyWeatherDto precipitation (int prec) {
		setPrecipitation(prec);
		return this;
	}
	
	public HourlyWeatherDto status (String status) {
		setStatus(status);
		return this;
	}
	
	public HourlyWeatherDto hourOfDay( int hour) {
		setHourOfDay(hour);
		return this;
	}
	public HourlyWeatherDto temperature (int temp) {
		setTemperature(temp);
		return this;
	}

	
	@Override
	public String toString() {
		return "HourlyWeatherDto [hourOfDay=" + hourOfDay + ", temperature=" + temperature + ", precipitation="
				+ precipitation + ", status=" + status + "]";
	}
	
	
}
