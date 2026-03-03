package com.skyapi.weatherforecast.hourly_weather.repository;

import org.springframework.data.repository.CrudRepository;

import com.skyapi.weatherforecast.common.HourlyWeather;
import com.skyapi.weatherforecast.common.HourlyWeatherID;

public interface HourlyWeatherRepository extends CrudRepository<HourlyWeather, HourlyWeatherID> {

}
