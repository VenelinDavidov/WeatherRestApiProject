package com.skyapi.weatherforecast.realtime.web;

import com.skyapi.weatherforecast.daily.web.DailyWeatherController;
import com.skyapi.weatherforecast.full.web.FullWeatherApiController;
import com.skyapi.weatherforecast.hourly_weather.web.HourlyWeatherAPIController;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skyapi.weatherforecast.common.Location;
import com.skyapi.weatherforecast.common.RealtimeWeather;
import com.skyapi.weatherforecast.location.exceptions.GeoLocationException;
import com.skyapi.weatherforecast.location.exceptions.LocationNotFoundException;
import com.skyapi.weatherforecast.location.service.GeoLocationService;
import com.skyapi.weatherforecast.realtime.service.RealtimeWeatherService;
import com.skyapi.weatherforecast.realtime.web.dto.RealtimeWeatherDTO;
import com.skyapi.weatherforecast.utility.CommonUtility;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/realtime")
public class RealtimeWeatherController {

	private Logger LOGGER = LoggerFactory.getLogger(RealtimeWeatherController.class);
	private GeoLocationService locationService;
	private RealtimeWeatherService realtimeWeatherService;
	private ModelMapper modelMapper;

	
	
	public RealtimeWeatherController(GeoLocationService locationService,
			                                               RealtimeWeatherService realtimeWeatherService,
			                                               ModelMapper modelMapper) {
		super();
		this.locationService = locationService;
		this.realtimeWeatherService = realtimeWeatherService;
		this.modelMapper = modelMapper;
	}

	@GetMapping
	public ResponseEntity<?> getRealtimeWeatherByIPAddress(HttpServletRequest request) {
		String ipAddress = CommonUtility.getIPAddress(request);

		try {
			Location locationFromIp = locationService.getLocation(ipAddress);
			RealtimeWeather realtimeWeather = realtimeWeatherService.getByLocation(locationFromIp);

			RealtimeWeatherDTO dto = modelMapper.map(realtimeWeather, RealtimeWeatherDTO.class);
			return ResponseEntity.ok(addLinksByIp (dto));

		} catch (GeoLocationException e) {
			LOGGER.error(e.getMessage(), e);
			return ResponseEntity.badRequest().build();

		} catch (LocationNotFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@GetMapping("/{locationCode}")
	public ResponseEntity<?> getRealtimeWeatherLocationCode(@PathVariable("locationCode") String locationCode) throws GeoLocationException {

		RealtimeWeather realtimeWeather = realtimeWeatherService.getByLocationCode (locationCode);
		RealtimeWeatherDTO dto = entity2Dto (realtimeWeather);
		return ResponseEntity.ok(addLinksByLocationCode (dto, locationCode));

	}
	
	
	@PutMapping("/{locationCode}")
	public ResponseEntity<?> updateRealtimeWeather(@PathVariable("locationCode") String locationCode,
			                             @RequestBody() @Valid RealtimeWeatherDTO dto) throws GeoLocationException {

		RealtimeWeather realtimeWeather = dto2Entity(dto);
		realtimeWeather.setLocationCode(locationCode);

		RealtimeWeather updatedRealtimeWeather = realtimeWeatherService.update(locationCode, realtimeWeather);
		RealtimeWeatherDTO updatedDTO = entity2Dto (updatedRealtimeWeather);

		return ResponseEntity.ok(addLinksByLocationCode (updatedDTO, locationCode));
	}


    // Methods for map class and dto
	private RealtimeWeather dto2Entity( RealtimeWeatherDTO dto) {
		return modelMapper.map (dto, RealtimeWeather.class);
	}

	private RealtimeWeatherDTO entity2Dto(RealtimeWeather realtimeWeather) {
		return modelMapper.map (realtimeWeather, RealtimeWeatherDTO.class);
	}


	private RealtimeWeatherDTO addLinksByIp(RealtimeWeatherDTO dto) throws GeoLocationException {
		dto.add (
				   WebMvcLinkBuilder.linkTo (WebMvcLinkBuilder.methodOn (RealtimeWeatherController.class)
						.getRealtimeWeatherByIPAddress (null))
						.withSelfRel ()
		);

		dto.add (
				WebMvcLinkBuilder.linkTo (WebMvcLinkBuilder.methodOn (HourlyWeatherAPIController.class)
						.listHourlyForecastByIPAdress (null))
						.withRel ("hourly_forecast")
		);

		dto.add (
				WebMvcLinkBuilder.linkTo (WebMvcLinkBuilder.methodOn (DailyWeatherController.class)
						.listDailyForecastByIPAddress (null))
						.withRel ("daily_forecast")
		);

		dto.add (
				WebMvcLinkBuilder.linkTo (WebMvcLinkBuilder.methodOn (FullWeatherApiController.class)
						.getFullWeatherByIPAddress (null))
						.withRel ("full_forecast")
		);

		return dto;
	}


	private RealtimeWeatherDTO addLinksByLocationCode(RealtimeWeatherDTO dto, String locationCode) throws GeoLocationException {
		dto.add (
				WebMvcLinkBuilder.linkTo (WebMvcLinkBuilder.methodOn (RealtimeWeatherController.class)
						.getRealtimeWeatherLocationCode (locationCode))
						.withSelfRel ()
		);

		dto.add (
				WebMvcLinkBuilder.linkTo (WebMvcLinkBuilder.methodOn (HourlyWeatherAPIController.class)
						.listHourlyWeatherForecastByLocationCode (locationCode, null))
						.withRel ("hourly_forecast")
		);

		dto.add (
				WebMvcLinkBuilder.linkTo (WebMvcLinkBuilder.methodOn (DailyWeatherController.class)
						.listDailyForecastByLocationCode (locationCode))
						.withRel ("daily_forecast")
		);

		dto.add (
				WebMvcLinkBuilder.linkTo (WebMvcLinkBuilder.methodOn (FullWeatherApiController.class)
								.getFullWeatherByLocationCode (locationCode))
						.withRel ("full_forecast")
		);

		return dto;
	}
}
