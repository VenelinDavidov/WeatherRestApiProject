package com.skyapi.weatherforecast.hourly_weather.web;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skyapi.weatherforecast.common.HourlyWeather;
import com.skyapi.weatherforecast.common.Location;
import com.skyapi.weatherforecast.hourly_weather.service.HourlyWeatherService;
import com.skyapi.weatherforecast.hourly_weather.web.dto.HourlyWeatherDto;
import com.skyapi.weatherforecast.hourly_weather.web.dto.HourlyWeatherListDto;
import com.skyapi.weatherforecast.location.exceptions.GeoLocationException;
import com.skyapi.weatherforecast.location.exceptions.LocationNotFoundException;
import com.skyapi.weatherforecast.location.service.GeoLocationService;
import com.skyapi.weatherforecast.utility.CommonUtility;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/v1/hourly")
public class HourlyWeatherAPIController {
	
	private final HourlyWeatherService hourlyWeatherService;
    private final GeoLocationService locationService;
    private final  ModelMapper modelMapper;
    
  
    public HourlyWeatherAPIController(HourlyWeatherService hourlyWeatherService, GeoLocationService locationService) {
		super();
		this.hourlyWeatherService = hourlyWeatherService;
		this.locationService = locationService;
		this.modelMapper = new ModelMapper();
	}
	
    
    
    @GetMapping
    public ResponseEntity<?> listHourlyForecastByIPAdress(HttpServletRequest request) throws LocationNotFoundException{
    	
    	String ipAddress = CommonUtility.getIPAddress(request);
    	
    	try {
    		
    		  int currenHour = Integer.parseInt(request.getHeader("X-Current-Hour"));
    	      Location locationFromIp = locationService.getLocation(ipAddress);
    	      List<HourlyWeather> hourlyForecast = hourlyWeatherService.getByLocation(locationFromIp, currenHour);
    	      
    	      if(hourlyForecast.isEmpty()){
    	    	  return ResponseEntity.noContent().build();
    	      }
    	      
    	          return ResponseEntity.ok(listEntity2Dto(hourlyForecast) );
    	
    	} catch (NumberFormatException | GeoLocationException ex){
    		
    		return ResponseEntity.badRequest().build();
    		
    	} catch (LocationNotFoundException ex) {
    		
    		return ResponseEntity.notFound().build();
    	}
    }
    
    
    
    
    private HourlyWeatherListDto listEntity2Dto (List<HourlyWeather> hourlyForecast) {
    	
    	Location location = hourlyForecast.get(0).getId().getLocation();
    	
    	HourlyWeatherListDto resultDto = new HourlyWeatherListDto();
    	resultDto.setLocation(location.toString());
    	
    	hourlyForecast.forEach(hourlyWeather ->{
    		HourlyWeatherDto dto = modelMapper.map(hourlyWeather, HourlyWeatherDto.class);
    		resultDto.addWeatherHourlyDto(dto);
    	});
    	
    	return resultDto;
    }
}
