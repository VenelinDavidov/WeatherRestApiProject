package com.skyapi.weatherforecast.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.skyapi.weatherforecast.common.HourlyWeather;
import com.skyapi.weatherforecast.hourly_weather.web.dto.HourlyWeatherDto;

@Configuration
public class ApplicationBeanConfiguration {

    @Bean
    ModelMapper modelMapper(){
		
		 ModelMapper modelMapper = new ModelMapper();
		 modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		 
		 var typeMap = modelMapper.typeMap(HourlyWeather.class, HourlyWeatherDto.class);
		 typeMap.addMapping(src-> src.getId().getHourDay(), HourlyWeatherDto::setHourDay);
		 
		 return modelMapper;
	}
}

