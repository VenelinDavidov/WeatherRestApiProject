package com.skyapi.weatherforecast.location;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.List;

import com.skyapi.weatherforecast.location.repository.LocationRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.skyapi.weatherforecast.common.HourlyWeather;
import com.skyapi.weatherforecast.common.Location;
import com.skyapi.weatherforecast.common.RealtimeWeather;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class LocationRepositoryTest {

	@Autowired
	private LocationRepository repository;

	@Test
	public void testAddSucsess() {
		Location location = new Location();
		location.setCode("NYC_MI");
		location.setCityName("Miammi");
		location.setRegionName("Aloha");
		location.setCountryCode("US");
		location.setCountryName("USA");
		location.setEnabled(true);

		Location savedLocation = repository.save(location);

		assertThat(savedLocation).isNotNull();
		assertThat(savedLocation.getCode()).isEqualTo("NYC_MI");

	}

	@Test
	public void testListSuccess() {

		List<Location> location = repository.findUntrashed();
		assertThat(location).isNotEmpty();
		location.forEach(System.out::println);
	}

	@Test
	public void testGetNotFound() {
		
		String code = "ABCD";
		Location location = repository.findByCode(code);
		
		assertThat(location).isNull();
	}

	
	
	@Test
	public void testGetCodeFound() {
		
		String code = "DELHI_IN";
		Location location = repository.findByCode(code);
		assertThat(location).isNotNull();
		assertThat(location.getCode()).isEqualTo(code);
	}
	
	
	@Test
	public void testTrashSuccess() {
		
		String code = "DELHI_IN";
		repository.trashByCode(code);
	    Location location = repository.findByCode(code);
	    assertThat(location).isNull();
	}
	
	
	@Test
	public void testAddRealtimeWeatherData() {
		
		String locationCode = "DELHI_IN";
		Location location = repository.findByCode(locationCode);
		
		RealtimeWeather realtimeWeather = location.getRealtimeWeather();
		
		if(realtimeWeather == null) {
			realtimeWeather = new RealtimeWeather();
			realtimeWeather.setLocation(location);
			location.setRealtimeWeather(realtimeWeather);
		}
		realtimeWeather.setTemperature(25);
		realtimeWeather.setHumidity(40);
		realtimeWeather.setPrecipitation(10);
		realtimeWeather.setStatus("Rain");
		realtimeWeather.setWindSpeed(115);
		realtimeWeather.setLastUpdated(new Date());
		
		Location updatedLocation = repository.save(location);
		assertThat(updatedLocation.getRealtimeWeather().getLocationCode()).isEqualTo(locationCode);
	}
	
	
	
	@Test
	public void testAddHourlyWeatherData() {
		
		Location location = repository.findById("DELHI_IN").get();
		
		List<HourlyWeather> listHourlyWeather = location.getListHourlyWeather();
		
		HourlyWeather forecast1 = new HourlyWeather()
				.id(location, 10)
				.temperature(-2)
				.precipitation(0)
				.status("Snowly");
		
		HourlyWeather forecast2 = new HourlyWeather()
				.id(location, 11)
				.temperature(-1)
				.precipitation(2)
				.status("Cloudy");
		
		listHourlyWeather.add(forecast1);
		listHourlyWeather.add(forecast2);
		
		Location updatedLocation = repository.save(location);
		
		assertThat(updatedLocation.getListHourlyWeather()).isNotEmpty();
	}
	
	
	@Test
	public void testFoundByCountryCodeAndCityNameNotFound() {
		String countryCode = "BZ";
		String cityName ="City";
		
		Location location = repository.findByCountryCodeAndCityName(countryCode, cityName);
		
		assertThat(location).isNull();
	}
	
	
	
	@Test
	public void testFoundByCountryCodeAndCityNameFound() {
		String countryCode = "US";
		String cityName ="New York City";
		
		Location location = repository.findByCountryCodeAndCityName(countryCode, cityName);
		
		assertThat(location).isNotNull();
		assertThat(location.getCountryCode()).isEqualTo(countryCode);
		assertThat(location.getCityName()).isEqualTo(cityName);
	}
}
