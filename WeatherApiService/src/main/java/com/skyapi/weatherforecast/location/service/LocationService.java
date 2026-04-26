package com.skyapi.weatherforecast.location.service;

import java.util.List;

import com.skyapi.weatherforecast.location.exceptions.LocationNotFoundException;
import com.skyapi.weatherforecast.location.repository.LocationRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

	@Deprecated
    public List<Location> list() {
		return repo.findUntrashed();
	}

	
	
	public Location get(String code) {
		 Location location = repo.findByCode(code);
		
		if(location == null) {
			throw new LocationNotFoundException(code);
		}
		return location;
	}
	
	
	public Page <Location> listByPage(int pageNum, int pageSize, String  sortField){

		Sort sort = Sort.by (sortField).ascending ();
		Pageable pageable = PageRequest.of (pageNum, pageSize, sort);

		return repo.findUntrashed (pageable);
	}
	
	
	public Location update(Location locationRequest){

		String code = locationRequest.getCode();

		Location locationInDB = repo.findByCode(code);

		if (locationInDB == null) {
			throw new LocationNotFoundException(code);
		}

		locationInDB.setCityName(locationRequest.getCityName());
		locationInDB.setRegionName(locationRequest.getRegionName());
		locationInDB.setCountryCode(locationRequest.getCountryName());
		locationInDB.setCityName(locationRequest.getCityName());
		locationInDB.setEnabled(locationRequest.isEnabled());

		return repo.save(locationInDB);
	}
	
	
	
	
	public void delete(String code)  {
	   
		Location location = repo.findByCode(code);
		if (location == null) {
			throw new LocationNotFoundException(code);
		}
		repo.trashByCode(code);
	}
}
