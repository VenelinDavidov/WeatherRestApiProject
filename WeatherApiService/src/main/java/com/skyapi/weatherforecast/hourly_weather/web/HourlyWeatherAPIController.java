package com.skyapi.weatherforecast.hourly_weather.web;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skyapi.weatherforecast.common.HourlyWeather;
import com.skyapi.weatherforecast.common.Location;
import com.skyapi.weatherforecast.hourly_weather.service.HourlyWeatherService;
import com.skyapi.weatherforecast.hourly_weather.web.dto.HourlyWeatherDto;
import com.skyapi.weatherforecast.hourly_weather.web.dto.HourlyWeatherListDto;
import com.skyapi.weatherforecast.hourly_weather.web.exception.BadRequestException;
import com.skyapi.weatherforecast.location.exceptions.GeoLocationException;
import com.skyapi.weatherforecast.location.exceptions.LocationNotFoundException;
import com.skyapi.weatherforecast.location.service.GeoLocationService;
import com.skyapi.weatherforecast.utility.CommonUtility;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/hourly")
@Validated
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
    		
    		  int currentHour = Integer.parseInt(request.getHeader("X-Current-Hour"));
    	      Location locationFromIp = locationService.getLocation(ipAddress);
    	      List<HourlyWeather> hourlyForecast = hourlyWeatherService.getByLocation(locationFromIp, currentHour);
    	      
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
    
    
    
    @GetMapping("/{locationCode}")
    public ResponseEntity<?> listHourlyWeatherForecastByLocationCode(@PathVariable("locationCode") String locationCode, 
    		                                                                                HttpServletRequest request){
    	try {
        int currentHour = Integer.parseInt(request.getHeader("X-Current-Hour"));	
    	List<HourlyWeather> hourlyForecast = hourlyWeatherService.getByLocationCode(locationCode, currentHour);
    	
    	if(hourlyForecast.isEmpty()) {
    		return ResponseEntity.noContent().build();
    	}
     	
     	return ResponseEntity.ok(listEntity2Dto(hourlyForecast));
     	
    	} catch (NumberFormatException ex) {
    		
    		return ResponseEntity.badRequest().build();
    		
    	} catch(LocationNotFoundException ex) {
    		
    		return ResponseEntity.notFound().build();
    	}
     }
    
    
    
    
    @PutMapping("/{locationCode}")
    public ResponseEntity<?> updateHourlyForecast(@PathVariable("locationCode") String locationCode,
    	@RequestBody @Valid List<HourlyWeatherDto> listDto) throws BadRequestException{
    	
    	if(listDto.isEmpty()) {
    		throw new BadRequestException("Hourly forecast data cannot be empty!");
    	}
    	

    	List<HourlyWeather> listHourlyWeather = listDto2ListEntity(listDto);
    	
    	listHourlyWeather.forEach(System.out::println);
    	
    	try {
			List<HourlyWeather> updateHourlyWeather = hourlyWeatherService.updateByLocationCode(locationCode, listHourlyWeather);
			
			return ResponseEntity.ok(listEntity2Dto(updateHourlyWeather));
			
		} catch (LocationNotFoundException e) {
			return ResponseEntity.notFound().build();
		}
    }
    
    
    
    
    private List<HourlyWeather> listDto2ListEntity(List<HourlyWeatherDto> listDto){

    	List<HourlyWeather> listEntity = new ArrayList<>();
    	
    	listDto.forEach(dto ->{
    		listEntity.add(modelMapper.map(dto, HourlyWeather.class));
    	});
    	
    	return listEntity;
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
