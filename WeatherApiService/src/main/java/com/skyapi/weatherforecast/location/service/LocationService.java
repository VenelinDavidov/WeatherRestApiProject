package com.skyapi.weatherforecast.location.service;

import java.util.List;

import com.skyapi.weatherforecast.location.exceptions.LocationNotFoundException;
import com.skyapi.weatherforecast.location.repository.LocationRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import com.skyapi.weatherforecast.common.Location;

@Service
@Transactional
public class LocationService {

	private LocationRepository repo;

	public LocationService(LocationRepository repo) {
		super();
		this.repo = repo;
	}

	public Location add(Location location) {
		return repo.save(location);
	}

	public List<Location> list() {
		return repo.findUntrashed();
	}

	public Location get(String code) {
		return repo.findByCode(code);
	}

	
	public Location update(Location locationRequest) throws LocationNotFoundException {

		String code = locationRequest.getCode();

		Location locationInDB = repo.findByCode(code);

		if (locationInDB == null) {
			throw new LocationNotFoundException("No location found with the given code!" + code);
		}

		locationInDB.setCityName(locationRequest.getCityName());
		locationInDB.setRegionName(locationRequest.getRegionName());
		locationInDB.setCountryCode(locationRequest.getCountryName());
		locationInDB.setCityName(locationRequest.getCityName());
		locationInDB.setEnabled(locationRequest.isEnabled());

		return repo.save(locationInDB);
	}
	
	
	
	
	public void delete(String code) throws LocationNotFoundException {
	   
		Location location = repo.findByCode(code);
		if (location == null) {
			throw new LocationNotFoundException("No location found with the given code!" + code);
		}
		repo.trashByCode(code);
	}
}
