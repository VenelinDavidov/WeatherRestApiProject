package com.skyapi.weatherforecast.config;

import com.skyapi.weatherforecast.common.Location;
import com.skyapi.weatherforecast.common.RealtimeWeather;
import com.skyapi.weatherforecast.full.web.dto.FullWeatherDTO;
import com.skyapi.weatherforecast.realtime.web.dto.RealtimeWeatherDTO;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;

import com.skyapi.weatherforecast.common.DailyWeather;
import com.skyapi.weatherforecast.common.HourlyWeather;
import com.skyapi.weatherforecast.daily.web.dto.DailyWeatherDto;
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
	      
	      
	      var typeMap3= modelMapper.typeMap(DailyWeather.class, DailyWeatherDto.class);
	      typeMap3.addMapping(type -> type.getId().getDayOfMonth(), DailyWeatherDto::setDayOfMonth);
	      typeMap3.addMapping(type -> type.getId().getMonth(),DailyWeatherDto::setMonth);


		  var typeMap4 = modelMapper.typeMap (DailyWeatherDto.class, DailyWeather.class);

		  typeMap4.addMapping (DailyWeatherDto::getDayOfMonth,
				  (dest, value) -> dest.getId ().setDayOfMonth (value != null ? (int) value : 0));

		  typeMap4.addMapping (DailyWeatherDto::getMonth,
				(dest,value) -> dest.getId ().setMonth (value != null ? (int) value :0));

		  var typeMap5 = modelMapper.typeMap (Location.class, FullWeatherDTO.class);
		  typeMap5.addMapping (Location::toString, FullWeatherDTO::setLocation);

		  var typeMap6 = modelMapper.typeMap (RealtimeWeatherDTO.class, RealtimeWeather.class);
		  typeMap6.addMappings (m -> m.skip (RealtimeWeather::setLocation));

		return modelMapper;
	}
    
    
}
 
