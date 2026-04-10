package com.skyapi.weatherforecast.realtime.web.dto;

import java.util.Date;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;


public class RealtimeWeatherDTO {


	private String location;

	@Range(min = -50, max = 50, message = "Temperature must be in the range!")
	private int temperature;

	@Range(min = 0, max = 100)
	private int humidity;

	@Range(min = 0, max = 100)
	private int precipitation;

	@JsonProperty("win_speed")
	private int windSpeed;

	@NotBlank(message = "Status must not be empty!")
	private String status;

	@JsonProperty("last_updated")
	private Date lastUpdated;
	
	
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public int getTemperature() {
		return temperature;
	}
	public void setTemperature(int temperature) {
		this.temperature = temperature;
	}
	public int getHumidity() {
		return humidity;
	}
	public void setHumidity(int humidity) {
		this.humidity = humidity;
	}
	public int getPrecipitation() {
		return precipitation;
	}
	public void setPrecipitation(int precipitation) {
		this.precipitation = precipitation;
	}
	public int getWindSpeed() {
		return windSpeed;
	}
	public void setWindSpeed(int windSpeed) {
		this.windSpeed = windSpeed;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getLastUpdated() {
		return lastUpdated;
	}
	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

}
