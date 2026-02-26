	package com.skyapi.weatherforecast.realtime;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.skyapi.weatherforecast.common.RealtimeWeather;
import com.skyapi.weatherforecast.realtime.repository.RealtimeWeatherRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class RealtimeWeatherRepositoryTest {

	@Autowired
	private RealtimeWeatherRepository repo;
	
	
	@Test
	public void testUpdate() {
		String locationCode = "NYC_USA";
		
		RealtimeWeather realtimeWeather = repo.findById(locationCode).get();
		
		realtimeWeather.setTemperature(-10);
		realtimeWeather.setHumidity(10);
		realtimeWeather.setPrecipitation(20);
		realtimeWeather.setStatus("Snowy");
		realtimeWeather.setWindSpeed(50);
		realtimeWeather.setLastUpdated(new Date());
		
		RealtimeWeather updateRealtimeWeather = repo.save(realtimeWeather);
		assertThat(updateRealtimeWeather.getHumidity()).isEqualTo(10);
	}
	
	@Test
	public void testFindByCountyCodeAndCityNotFound() {
		String countyCode = "JP";
		String cityName = "Tokyo";
		
		RealtimeWeather realtimeWeather = repo.findByCountryCodeAndCity(countyCode, cityName);
		
		assertThat(realtimeWeather).isNull();
	}
	
	@Test
	public void testFindByCountyCodeAndCityFound() {
		String countyCode = "US";
		String cityName = "New York City";
		
		RealtimeWeather realtimeWeather = repo.findByCountryCodeAndCity(countyCode, cityName);
		
		assertThat(realtimeWeather).isNotNull();
		assertThat(realtimeWeather.getLocation().getCityName()).isEqualTo(cityName);
				
	}
	
	
	@Test 
	public void testFindByLocationNotFound() {
		String locationCode = "ABCXH";
		RealtimeWeather realtimeWeather = repo.findByLocationCode(locationCode);
		
		assertThat(realtimeWeather).isNull();
	
	}
	
	@Test 
	public void testFindByTrashedLocationNotFound() {
		String locationCode = "NYC_USA";
		RealtimeWeather realtimeWeather = repo.findByLocationCode(locationCode);
		
		assertThat(realtimeWeather).isNull();
	
	}
	
	@Test 
	public void testFindByLocationFound() {
		String locationCode = "NYC_USA";
		RealtimeWeather realtimeWeather = repo.findByLocationCode(locationCode);
		
		assertThat(realtimeWeather).isNotNull();
	    assertThat(realtimeWeather.getLocationCode()).isEqualTo(locationCode);
	}
	
}
