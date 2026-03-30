package com.skyapi.weatherforecast.daily;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import com.skyapi.weatherforecast.common.DailyWeather;
import com.skyapi.weatherforecast.common.DailyWeatherId;
import com.skyapi.weatherforecast.common.Location;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class DailyWeatherRepositoryTests {

	private @Autowired DailyWeatherRepository repo;
	
	
	@Test
	public void testAdd() {
		
		String locationCode = "NYC_USA";
		
		Location location = new Location().code(locationCode);
		
		DailyWeather forecast1 = new DailyWeather ()
				.location(location)
				.dayOfMonth(20)
				.month(15)
				.minTemp(12)
				.maxTemp(25)
				.precipitation(24)
				.status("Clear");
		
		DailyWeather addedForecast = repo.save(forecast1);
		
		assertThat(addedForecast.getId().getLocation().getCode()).isEqualTo(locationCode);
	}
	
	
	@Test
	public void testDelete() {
		String locationCode = "NYC_USA";
		Location location = new Location().code(locationCode);
		DailyWeatherId id = new DailyWeatherId(16,7,location);
		
		repo.deleteById(id);
		
		Optional<DailyWeather> result = repo.findById(id);
		
		assertThat(result).isNotPresent();
	}
}
