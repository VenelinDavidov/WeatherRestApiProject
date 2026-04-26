package com.skyapi.weatherforecast.location.web;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import com.skyapi.weatherforecast.location.service.LocationService;
import com.skyapi.weatherforecast.location.web.dto.LocationDto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.skyapi.weatherforecast.common.Location;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/locations")
@Validated
public class LocationApiController {

	private LocationService locationService;
	private ModelMapper modelMapper;

	
	

	public LocationApiController(LocationService locationService, ModelMapper modelMapper) {
		super();
		this.locationService = locationService;
		this.modelMapper = modelMapper;
	}


	@PostMapping
	public ResponseEntity<Object> addLocation(@RequestBody @Valid LocationDto dto) {

		Location addedLocation = locationService.add(dto2Entity(dto));
		URI uri = URI.create("/v1/locations/" + addedLocation.getCode());

		return ResponseEntity.created(uri).body(entity2Dto(addedLocation));
	}
	
	

	@Deprecated
	public ResponseEntity<?> listLocation() {

		List<Location> locations = locationService.list();

		if (locations.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(listEntity2ListDto(locations));
	}


	@GetMapping
	public ResponseEntity<?> listLocations(@RequestParam (value = "page", required = false, defaultValue = "1")
											 @Min(value = 1)  Integer pageNum,

										   @RequestParam (value = "size", required = false, defaultValue = "4")
										     @Min (value = 1) @Max(value = 20) Integer pageSize,

										   @RequestParam (value = "sort", required = false, defaultValue = "code") String sortField) {

		Page <Location> page = locationService.listByPage (pageNum - 1, pageSize, sortField);

		List <Location> locations = page.getContent ();

		if (locations.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok( listEntity2ListDto(locations));
	}

//	private CollectionModel <LocationDto> addPageMetadataAndLinks2Collection(List <LocationDto> listDto, Page <Location> page) {
//
//		int pageSize =  page.getSize ();
//		int pageNum = page.getNumber () + 1;
//
//		long totalElements = page.getTotalElements ();
//
//		PagedModel.PageMetadata pageMetaData = new PagedModel.PageMetadata (pageSize, pageNum, totalElements);
//
//		CollectionModel<LocationDto> collectionModel = PagedModel.of (listDto, pageMetaData);
//
//		return collectionModel;
//	}


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
      
      
      private List <Object> listEntity2ListDto(List<Location> listEntity){
    	  return listEntity
    			  .stream()
    			  .map(this::entity2Dto)
    			  .collect(Collectors.toList()).reversed ();
      }



  	  private Object entity2Dto(Location entity) {
  		return modelMapper.map(entity, LocationDto.class);
  	}


}
