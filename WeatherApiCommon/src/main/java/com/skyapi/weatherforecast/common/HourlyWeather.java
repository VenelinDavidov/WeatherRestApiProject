package com.skyapi.weatherforecast.common;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;


@Entity
@Table(name = "weather_hourly")
public class HourlyWeather {

	
	@EmbeddedId
	private HourlyWeatherID id = new HourlyWeatherID();
	
	private int temperature;
	
	private int precipitation;
	
	@Column(length = 50)
	private String status;
	
	
	

	
	public HourlyWeatherID getId() {
		return id;
	}

	public void setId(HourlyWeatherID id) {
		this.id = id;
	}
	

	public int getHourOfDay() {
	    return id.getHourOfDay();
	}
	
	public void setHourOfDay(int hourOfDay) {
	    id.setHourOfDay(hourOfDay);
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
	
	
	
	
	
	//Get fields and use them
	public HourlyWeather temperature (int temp) {
		setTemperature(temp);
		return this;
	}
	
	public HourlyWeather precipitation (int prec) {
		setPrecipitation(prec);
		return this;
	}
	
	public HourlyWeather status (String status) {
		setStatus(status);
		return this;
	}
	
	
	public HourlyWeather id(Location location, int hour) {
		this.id.setHourOfDay(hour);
		this.id.setLocation(location);
		return this;
	}
}
