package com.skyapi.weatherforecast.location.web;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import com.skyapi.weatherforecast.location.exceptions.LocationNotFoundException;
import com.skyapi.weatherforecast.location.service.LocationService;
import com.skyapi.weatherforecast.location.web.dto.LocationDto;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skyapi.weatherforecast.common.Location;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/locations")
public class LocationApiController {

	private LocationService locationService;
	private ModelMapper modelMapper;

	
	

	public LocationApiController(LocationService locationService, ModelMapper modelMapper) {
		super();
		this.locationService = locationService;
		this.modelMapper = modelMapper;
	}


	@PostMapping
	public ResponseEntity<LocationDto> addLocation(@RequestBody @Valid LocationDto dto) {

		Location addedLocation = locationService.add(dto2Entity(dto));
		URI uri = URI.create("/v1/locations/" + addedLocation.getCode());

		return ResponseEntity.created(uri).body(entity2Dto(addedLocation));
	}
	
	
	

	@GetMapping
	public ResponseEntity<?> listLocation() {

		List<Location> locations = locationService.list();

		if (locations.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(listEntity2ListDto(locations));
	}
	
	
	@GetMapping("/{code}")
	public ResponseEntity<?> getLocation(@PathVariable("code") String code) {

		Location location = locationService.get(code);


		return ResponseEntity.ok(entity2Dto(location));
	}

	
	@PutMapping
	public ResponseEntity<?> updateLocation(@RequestBody @Valid LocationDto dto) {

			Location updatedLocation = locationService.update(dto2Entity(dto));
			return ResponseEntity.ok(entity2Dto(updatedLocation));
	}
	
	
	@DeleteMapping("/{code}")
	public ResponseEntity<?> deleteLocation(@PathVariable("code") String code) {

			locationService.delete(code);
			return ResponseEntity.noContent().build();
		
	}
	
	
	
	
      private Location dto2Entity(@Valid LocationDto dto) {
		
		return modelMapper.map(dto, Location.class); 
	}
      
      
      private List<Object> listEntity2ListDto(List<Location> listEntity){
    	  return listEntity
    			  .stream()
    			  .map(entity -> entity2Dto(entity))
    			  .collect(Collectors.toList());
      }



  	  private Object entity2Dto(Location entity) {
  		return modelMapper.map(entity, LocationDto.class);
  	}


}
