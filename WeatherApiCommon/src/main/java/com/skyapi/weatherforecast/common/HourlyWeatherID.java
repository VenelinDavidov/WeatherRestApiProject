package com.skyapi.weatherforecast.common;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Embeddable
public class HourlyWeatherID  implements Serializable{
 
	@Column(name = "hour_of_day")
	private int hourDay;
	
	@ManyToOne
	@JoinColumn(name = "location_code")
	private Location location;
	
	
	
	public HourlyWeatherID() {}
	
	
	public HourlyWeatherID(int hourDay, Location location) {
		super();
		this.hourDay = hourDay;
		this.location = location;
	}
	
	
	public int getHourDay() {
		return hourDay;
	}
	public void setHourDay(int hourDay) {
		this.hourDay = hourDay;
	}
	public Location getLocation() {
		return location;
	}
	public void setLocation(Location location) {
		this.location = location;
	}
	
	
}
