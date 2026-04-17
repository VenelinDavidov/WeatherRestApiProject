package com.skyapi.weatherforecast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.skyapi.weatherforecast.common.DailyWeather;
import com.skyapi.weatherforecast.common.HourlyWeather;
import com.skyapi.weatherforecast.common.Location;
import com.skyapi.weatherforecast.common.RealtimeWeather;
import com.skyapi.weatherforecast.daily.web.dto.DailyWeatherDto;
import com.skyapi.weatherforecast.full.web.dto.FullWeatherDTO;
import com.skyapi.weatherforecast.hourly_weather.web.dto.HourlyWeatherDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class WeatherApiServiceApplication {

//	@Bean
//    public ModelMapper getModelMapper(){
//		ModelMapper modelMapper = new ModelMapper ();
//		modelMapper.getConfiguration ().setMatchingStrategy (MatchingStrategies.STRICT);
//
//		configureMappingForHourlyWeather(modelMapper);
//		configureMappingForDailyWeather(modelMapper);
//		configureMappingForFullWeather(modelMapper);
//		configureMappingForRealTimeWeather(modelMapper);
//
//		return modelMapper;
//	}
//
//	private void configureMappingForRealTimeWeather(ModelMapper modelMapper) {
//		modelMapper.typeMap (RealtimeWeather.class, RealtimeWeather.class)
//				.addMappings (m->m.skip (RealtimeWeather::setLocation));
//	}
//
//	private void configureMappingForFullWeather(ModelMapper modelMapper) {
//		modelMapper.typeMap (Location.class, FullWeatherDTO.class)
//				.addMapping (src -> src.toString (), FullWeatherDTO::setLocation);
//	}
//
//	private void configureMappingForDailyWeather(ModelMapper modelMapper) {
//		modelMapper.typeMap (DailyWeather.class, DailyWeatherDto.class)
//				.addMapping (src -> src.getId ().getDayOfMonth (), DailyWeatherDto::setDayOfMonth)
//				.addMapping (src -> src.getId ().getMonth (), DailyWeatherDto::setMonth);
//
//		modelMapper.typeMap (DailyWeatherDto.class, DailyWeather.class)
//				.addMapping (DailyWeatherDto::getDayOfMonth,
//						(dest,value) -> dest.getId ().setDayOfMonth (value != null ? (int) value : 0))
//
//				.addMapping (src -> src.getMonth (),
//						(dest,value) -> dest.getId ().setMonth (value != null ? (int) value : 0));
//	}
//
//	private void configureMappingForHourlyWeather(ModelMapper modelMapper) {
//		modelMapper.typeMap (HourlyWeather.class, HourlyWeatherDto.class)
//				.addMapping (src -> src.getId ().getHourOfDay (), HourlyWeatherDto::setHourOfDay);
//
//		modelMapper.typeMap (HourlyWeatherDto.class, HourlyWeather.class)
//				.addMapping (HourlyWeatherDto::getHourOfDay,
//						(dest,value)->  dest.getId ().setHourOfDay (value != null ? (int) value : 0));
//	}
//
//    @Bean
//	public ObjectMapper objectMapper(){
//		ObjectMapper objectMapper = new ObjectMapper ();
//        objectMapper.enable (SerializationFeature.INDENT_OUTPUT);
//		objectMapper.setPropertyNamingStrategy (PropertyNamingStrategy.SNAKE_CASE);
//		return objectMapper;
//	}


	public static void main(String[] args) {
		SpringApplication.run(WeatherApiServiceApplication.class, args);
	}

}
