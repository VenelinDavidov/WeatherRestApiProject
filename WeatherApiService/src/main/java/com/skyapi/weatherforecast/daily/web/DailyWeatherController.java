package com.skyapi.weatherforecast.daily.web;

import java.util.ArrayList;
import java.util.List;

import com.skyapi.weatherforecast.hourly_weather.web.exception.BadRequestException;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.skyapi.weatherforecast.common.DailyWeather;
import com.skyapi.weatherforecast.common.Location;
import com.skyapi.weatherforecast.daily.service.DailyWeatherService;
import com.skyapi.weatherforecast.daily.web.dto.DailyWeatherDto;
import com.skyapi.weatherforecast.daily.web.dto.DailyWeatherListDto;
import com.skyapi.weatherforecast.location.exceptions.GeoLocationException;
import com.skyapi.weatherforecast.location.service.GeoLocationService;
import com.skyapi.weatherforecast.utility.CommonUtility;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/v1/daily")
public class DailyWeatherController {

	private DailyWeatherService dailyWeatherService;
	private GeoLocationService locationService;
	private ModelMapper mapper;

	public DailyWeatherController(DailyWeatherService dailyWeatherService, GeoLocationService locationService,
			ModelMapper mapper) {
		super();
		this.dailyWeatherService = dailyWeatherService;
		this.locationService = locationService;
		this.mapper = mapper;
	}

	@GetMapping
	public ResponseEntity<?> listDailyForecastByIPAdress(HttpServletRequest request) throws GeoLocationException {
		String ipAddress = CommonUtility.getIPAddress(request);

		Location locationFromIP = locationService.getLocation(ipAddress);
		List<DailyWeather> dailyForecast = dailyWeatherService.getByLocation(locationFromIP);

		if (dailyForecast.isEmpty()) {
			return ResponseEntity.noContent().build();
		}

		return ResponseEntity.ok(listEntity2DTO(dailyForecast));
	}

	
	@GetMapping("/{locationCode}")
    public ResponseEntity<?> listHourlyForecastByLocationCode(@PathVariable ("LocationCode") String locationCode) {
		List<DailyWeather> dailyForecast = dailyWeatherService.getByLocationCode(locationCode);
		
		if (dailyForecast == null) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(listEntity2DTO(dailyForecast)); 
	}


    @PutMapping("/{locationCode}")
	public ResponseEntity<?> updateDailyWeatherForecast(@PathVariable("locationCode") String code,
														@RequestBody @Valid List<DailyWeatherDto> listDto) throws BadRequestException {

		if (listDto.isEmpty ()){
			throw new BadRequestException ("Daily forecast data cannot be empty!");
		}

		listDto.forEach (System.out::println);

		List<DailyWeather> dailyWeathers = listDTO2ListEntity(listDto);

		System.out.println ("======================");
		List <DailyWeather> updatedForecast = dailyWeatherService.updateByLocationCode (code, dailyWeathers);

		return ResponseEntity.ok (listEntity2DTO (updatedForecast));
	}


	private List <DailyWeather> listDTO2ListEntity(List <DailyWeatherDto> listDto) {
		List<DailyWeather> listEntity = new ArrayList <> ();

		listDto.forEach (dto -> {
			listEntity.add (mapper.map (dto, DailyWeather.class));
		});
		return listEntity;
	}


	private DailyWeatherListDto listEntity2DTO(List<DailyWeather> dailyForecast) {

		Location location = dailyForecast.get(0).getId().getLocation();

		DailyWeatherListDto listDto = new DailyWeatherListDto();
		listDto.setLocation(location.toString());

		dailyForecast.forEach(dailyWeather -> {
			listDto.addDailyWeatherDto(mapper.map(dailyWeather, DailyWeatherDto.class));
		});
		return listDto;

	}
}
