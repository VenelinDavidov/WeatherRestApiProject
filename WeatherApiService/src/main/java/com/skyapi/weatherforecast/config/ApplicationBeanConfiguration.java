package com.skyapi.weatherforecast.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;

import com.skyapi.weatherforecast.common.HourlyWeather;
import com.skyapi.weatherforecast.hourly_weather.web.dto.HourlyWeatherDto;



@Configuration
public class ApplicationBeanConfiguration {

    @Bean
    ModelMapper modelMapper(){
		
		 ModelMapper modelMapper = new ModelMapper();
		 modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		 
//		 var typeMap = modelMapper.typeMap(HourlyWeather.class, HourlyWeatherDto.class);
//		 typeMap.addMapping(src-> src.getId().getHourOfDay(), HourlyWeatherDto::setHourOfDay);
		 
		 var typeMap1 = modelMapper.typeMap(HourlyWeather.class, HourlyWeatherDto.class);
		
	      typeMap1.addMappings(mapper -> mapper.map(
	            src -> src.getId().getHourOfDay(),
	            HourlyWeatherDto::setHourOfDay
	        ));
	      
	      var typeMap2 = modelMapper.typeMap(HourlyWeatherDto.class, HourlyWeather.class);
	      typeMap2.addMapping(model -> model.getHourOfDay(), (dest, value) -> dest.getId().setHourOfDay(value != null ? (int) value : 0));
	      
		 
		 return modelMapper;
	}
    
    
}
 
