package com.skyapi.weatherforecast.realtime.service;

import org.springframework.stereotype.Service;

import com.skyapi.weatherforecast.common.Location;
import com.skyapi.weatherforecast.common.RealtimeWeather;
import com.skyapi.weatherforecast.location.exceptions.LocationNotFoundException;
import com.skyapi.weatherforecast.realtime.repository.RealtimeWeatherRepository;

@Service
public class RealtimeWeatherService {
	
	private RealtimeWeatherRepository repository;

	
	public RealtimeWeatherService(RealtimeWeatherRepository repository) {
		super();
		this.repository = repository;
	}

	
	
	public RealtimeWeather getByLocation (Location location) throws LocationNotFoundException {
		
		String countryCode = location.getCountryCode();
		String cityName = location.getCityName();
		
		RealtimeWeather realtimeWeather = repository.findByCountryCodeAndCity(countryCode, cityName);
		
		if(realtimeWeather == null) {
			throw new LocationNotFoundException("No location found with country code and city name");
		}
		return realtimeWeather;
	}
}
