package com.skyapi.weatherforecast.realtime.service;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.skyapi.weatherforecast.common.Location;
import com.skyapi.weatherforecast.common.RealtimeWeather;
import com.skyapi.weatherforecast.location.exceptions.LocationNotFoundException;
import com.skyapi.weatherforecast.location.repository.LocationRepository;
import com.skyapi.weatherforecast.realtime.repository.RealtimeWeatherRepository;

@Service
public class RealtimeWeatherService {
	
	private RealtimeWeatherRepository repository;
	private LocationRepository locationRepository;

	
	public RealtimeWeatherService(RealtimeWeatherRepository repository, LocationRepository locationRepository) {
		super();
		this.repository = repository;
		this.locationRepository = locationRepository;
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


	
	
	public RealtimeWeather getByLocationCode(String locationCode) throws LocationNotFoundException {
		
		RealtimeWeather realtimeWeather = repository.findByLocationCode(locationCode);
		
		if(realtimeWeather == null ) {
			throw new LocationNotFoundException("No location found with given code: " + locationCode);
		}
		return realtimeWeather;
	}
	
	
	
	
	public RealtimeWeather update(String locationCode, RealtimeWeather realtimeWeather) throws LocationNotFoundException {
		 
		Location location = locationRepository.findByCode(locationCode);
		
		if(location == null ) {
			throw new LocationNotFoundException("No location found witn given code: " + locationCode);
		}
		
		realtimeWeather.setLocation(location);
		realtimeWeather.setLastUpdated(new Date());
		
		if(location.getRealtimeWeather() == null) {
			location.setRealtimeWeather(realtimeWeather);
			Location updatedLocation = locationRepository.save(location);
			
			return updatedLocation.getRealtimeWeather();
		}
		
		return repository.save(realtimeWeather);
		
	}
}
