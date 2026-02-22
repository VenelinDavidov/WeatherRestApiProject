package com.skyapi.weatherforecast.realtime;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;

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
	

}
