package com.skyapi.weatherforecast.hourly_weather.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.skyapi.weatherforecast.common.HourlyWeather;
import com.skyapi.weatherforecast.common.HourlyWeatherID;

public interface HourlyWeatherRepository extends CrudRepository<HourlyWeather, HourlyWeatherID> {
	
	@Query("""
			SELECT h FROM HourlyWeather h WHERE
			h.id.location.code = ?1 AND h.id.hourDay > ?2
			AND h.id.location.trashed = false
			""")
	public List<HourlyWeather> findByLocationCode (String locationCode, int currentHour);

}
