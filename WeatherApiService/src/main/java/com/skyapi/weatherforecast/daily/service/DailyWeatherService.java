package com.skyapi.weatherforecast.daily.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import com.skyapi.weatherforecast.common.DailyWeather;
import com.skyapi.weatherforecast.common.Location;
import com.skyapi.weatherforecast.daily.repo.DailyWeatherRepository;
import com.skyapi.weatherforecast.location.exceptions.LocationNotFoundException;
import com.skyapi.weatherforecast.location.repository.LocationRepository;

@Service
public class DailyWeatherService {
	
	private DailyWeatherRepository dailyWeatherRepo;
	private LocationRepository  locationRepo;
	
	
	
	public DailyWeatherService(DailyWeatherRepository dailyWeatherRepo, LocationRepository locationRepo) {
		super();
		this.dailyWeatherRepo = dailyWeatherRepo;
		this.locationRepo = locationRepo;
	}
	
	
	
	
	public List<DailyWeather> getByLocation (Location location){
		
		String countryCode = location.getCountryCode();
		String cityName = location.getCityName();
		
		Location locationDB = locationRepo.findByCountryCodeAndCityName(countryCode, cityName);
		
		if(locationDB == null) {
			throw new LocationNotFoundException(countryCode,cityName);
		}
		
		return dailyWeatherRepo.findByLocationCode(locationDB.getCode());
	}
	
	
	

	
	public List<DailyWeather> getByLocationCode(String locationCode) {
		
		Location location = locationRepo.findByCode(locationCode);
		
		if (location == null) {
			throw  new LocationNotFoundException(locationCode);
		}
		
		return dailyWeatherRepo.findByLocationCode(locationCode);
	}


	public List<DailyWeather> updateByLocationCode(String code, List<DailyWeather> dailyWeatherInRequest){

		Location location = locationRepo.findByCode (code);

		if (location == null){
			throw new LocationNotFoundException (code);
		}

		for (DailyWeather data : dailyWeatherInRequest) {
			data.getId ().setLocation (location);
		}

		List <DailyWeather> dailyWeatherForDB = location.getListDailyWeather ();
		List<DailyWeather> dailyWeatherToBeRemoved = new ArrayList <> ();

		for (DailyWeather forecast : dailyWeatherForDB) {
			if (!dailyWeatherInRequest.contains (forecast)){
				dailyWeatherToBeRemoved.add (forecast.getShallowCopy());
			}
		}

		for (DailyWeather forecastToBeRemoved : dailyWeatherToBeRemoved) {
			dailyWeatherForDB.remove (forecastToBeRemoved);
		}

		return (List<DailyWeather>)  dailyWeatherRepo.saveAll (dailyWeatherInRequest);
	}
}
