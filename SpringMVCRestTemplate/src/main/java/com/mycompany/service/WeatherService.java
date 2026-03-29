package com.mycompany.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import com.mycompany.entity.RealtimeWeather;
import com.mycompany.exception.WeatherServiceException;

@Service
public class WeatherService {

	@Value("${api.weather.realtime.get.uri}")
	private String getRealtimeWeatherRequestURI;

	@Autowired
	private RestTemplate restTemplate;

	
	

	
	public RealtimeWeather getRealtimeWeather() throws WeatherServiceException {

		try {
			return restTemplate.getForObject(getRealtimeWeatherRequestURI, RealtimeWeather.class);
			
		} catch (RestClientResponseException ex) {
			
			String message = "Error calling Get Realtime Weather Api: " + ex.getMessage();
			ex.printStackTrace();
			throw new WeatherServiceException(message);
		}
	}
}
