package com.skyapi.weatherforecast.realtime.web;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skyapi.weatherforecast.common.Location;
import com.skyapi.weatherforecast.common.RealtimeWeather;
import com.skyapi.weatherforecast.config.ApplicationBeanConfiguration;
import com.skyapi.weatherforecast.location.exceptions.GeoLocationException;
import com.skyapi.weatherforecast.location.exceptions.LocationNotFoundException;
import com.skyapi.weatherforecast.location.service.GeoLocationService;
import com.skyapi.weatherforecast.realtime.service.RealtimeWeatherService;
import com.skyapi.weatherforecast.realtime.web.dto.RealtimeWeatherDTO;
import com.skyapi.weatherforecast.utility.CommonUtility;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/v1/realtime")
public class RealtimeWeatherController {

	private Logger LOGGER = LoggerFactory.getLogger(RealtimeWeatherController.class);
	private GeoLocationService locationServicel;
	private RealtimeWeatherService realtimeWeatherService;
	private ModelMapper modelMapper;



	public RealtimeWeatherController(GeoLocationService locationServicel, RealtimeWeatherService realtimeWeatherService,
			ModelMapper modelMapper) {
		super();
		this.locationServicel = locationServicel;
		this.realtimeWeatherService = realtimeWeatherService;
		this.modelMapper = modelMapper;
	}

	@GetMapping
	public ResponseEntity<?> getRealtimeWeatherByIPAddress(HttpServletRequest request) {
		String ipAddress = CommonUtility.getIPAddress(request);

		try {
			Location locationFromIp = locationServicel.getLocation(ipAddress);
			RealtimeWeather realtimeWeather = realtimeWeatherService.getByLocation(locationFromIp);
			
			RealtimeWeatherDTO dto = modelMapper.map(realtimeWeather, RealtimeWeatherDTO.class);
			return ResponseEntity.ok(dto);
			
		} catch (GeoLocationException e) {
			LOGGER.error(e.getMessage(), e);
			return ResponseEntity.badRequest().build();

		} catch (LocationNotFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}
	
	@GetMapping("/{locationCode}")
	public ResponseEntity<?> getRealtimeWeatherLocationCode(@PathVariable("locationCode") String locationCode){
		
		try {
			RealtimeWeather realtimeWeather = realtimeWeatherService.getByLocationCode(locationCode);
			RealtimeWeatherDTO dto = modelMapper.map(realtimeWeather, RealtimeWeatherDTO.class);
			
			return ResponseEntity.ok(dto);
		} catch (LocationNotFoundException e) {
			
			LOGGER.error(e.getMessage(),e);
			return ResponseEntity.notFound().build();
		}
	}
}
