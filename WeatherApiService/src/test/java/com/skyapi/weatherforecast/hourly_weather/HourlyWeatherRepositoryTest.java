package com.skyapi.weatherforecast.hourly_weather;

import com.skyapi.weatherforecast.hourly_weather.repository.HourlyWeatherRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import com.skyapi.weatherforecast.common.HourlyWeather;
import com.skyapi.weatherforecast.common.HourlyWeatherID;
import com.skyapi.weatherforecast.common.Location;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class HourlyWeatherRepositoryTest {

	@Autowired
	private HourlyWeatherRepository hourlyWeatherRepository;
	
	
	@Test
	public void testAdd() {
		
		String locationCode = "NYC_MI";
		int hourOfDay = 14;
		
		Location location = new Location().code(locationCode);
		
		HourlyWeather forecast = new HourlyWeather()
				.id(location, hourOfDay)
				.temperature(20)
				.precipitation(30)
				.status("Sunny");
		
	   HourlyWeather updateForecast = hourlyWeatherRepository.save(forecast);
	   
	   assertThat(updateForecast.getId().getLocation().getCode()).isEqualTo(locationCode);
	   assertThat(updateForecast.getId().getHourDay()).isEqualTo(hourOfDay);

	}
	
	
	@Test
	public void testDelete() {
		
		Location location = new Location().code("NYC_MI");
		HourlyWeatherID id = new HourlyWeatherID( 9, location );
		
		hourlyWeatherRepository.deleteById(id);
		
		Optional<HourlyWeather> optionalResult = hourlyWeatherRepository.findById(id);
		assertThat(optionalResult).isPresent();
	}
}
