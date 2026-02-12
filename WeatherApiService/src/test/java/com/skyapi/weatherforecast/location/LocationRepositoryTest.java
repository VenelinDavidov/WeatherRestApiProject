package com.skyapi.weatherforecast.location;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import com.skyapi.weatherforecast.location.repository.LocationRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.skyapi.weatherforecast.common.Location;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class LocationRepositoryTest {

	@Autowired
	private LocationRepository repository;

	@Test
	public void testAddSucsess() {
		Location location = new Location();
		location.setCode("NYC_USA");
		location.setCityName("New York City");
		location.setRegionName("New York");
		location.setCountryCode("US");
		location.setCountryName("USA");
		location.setEnabled(true);

		Location savedLocation = repository.save(location);

		assertThat(savedLocation).isNotNull();
		assertThat(savedLocation.getCode()).isEqualTo("NYC_USA");

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

}
