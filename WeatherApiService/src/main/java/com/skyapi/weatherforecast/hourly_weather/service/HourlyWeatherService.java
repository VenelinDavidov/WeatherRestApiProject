package com.skyapi.weatherforecast.hourly_weather.service;

import java.util.ArrayList;
import java.util.Collections;
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
		
		return hourlyWeatherRepository.findByLocationCode(locationOfDB.getCode(), currentHour);
	}
	
	
	
	public List<HourlyWeather> getByLocationCode (String locationCode, int currentHour) throws LocationNotFoundException{
		
		Location location = locationRepository.findByCode(locationCode);
		if(location == null ) {
			throw new LocationNotFoundException("No location foind with the given code.");
		}
		return hourlyWeatherRepository.findByLocationCode(locationCode, currentHour);
	}
	
	
	
	
     public List<HourlyWeather> updateByLocationCode(String locationCode, List<HourlyWeather> hourlyForecastInRequest) throws LocationNotFoundException{
		
    	 Location location = locationRepository.findByCode(locationCode);
    	  
    	 if(location == null) {
    		 throw new LocationNotFoundException("No location found with the given code:" + locationCode);
    	 }
    	 
    	 for(HourlyWeather item : hourlyForecastInRequest) {
    		 item.getId().setLocation(location);
    	 }
    	 
    	 List<HourlyWeather> listHourlyWeatherDB = location.getListHourlyWeather();
    	 List<HourlyWeather> hourlyWeatherToBeRemoved = new ArrayList<>();
    	 
    	 for(HourlyWeather item: hourlyForecastInRequest) {
    		 if(!hourlyForecastInRequest.contains(item)) {
    			 hourlyWeatherToBeRemoved.add(item);
    		 }
    	 }
    	 
    	 for (HourlyWeather item: hourlyWeatherToBeRemoved) {
    		 listHourlyWeatherDB.remove(item);
    	 }
    	 
    	  return (List<HourlyWeather>) hourlyWeatherRepository.saveAll(hourlyForecastInRequest);
	}

}
