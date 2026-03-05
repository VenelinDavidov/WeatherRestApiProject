package com.skyapi.weatherforecast.hourly_weather.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.skyapi.weatherforecast.common.HourlyWeather;
import com.skyapi.weatherforecast.common.Location;
import com.skyapi.weatherforecast.hourly_weather.repository.HourlyWeatherRepository;
import com.skyapi.weatherforecast.location.exceptions.LocationNotFoundException;
import com.skyapi.weatherforecast.location.repository.LocationRepository;

@Service
public class HourlyWeatherService {
	
	private final HourlyWeatherRepository hourlyWeatherRepository;
	private final LocationRepository locationRepository;
	
    
	public HourlyWeatherService(HourlyWeatherRepository hourlyWeatherRepository,
			                        LocationRepository locationRepository) {
		super();
		this.hourlyWeatherRepository = hourlyWeatherRepository;
		this.locationRepository = locationRepository;
	}
	
	
	
	public List<HourlyWeather> getByLocation(Location location, int currentHour) throws LocationNotFoundException{
		
		String countryCode = location.getCountryCode();
		String cityName = location.getCityName();
		
		Location locationOfDB = locationRepository.findByCountryCodeAndCityName(countryCode, cityName);
		
		if(locationOfDB == null) {
			throw new LocationNotFoundException("No location found! When given country code and city name");
		}
		
		return hourlyWeatherRepository.findByLocationCode(cityName, currentHour);
	}
}
