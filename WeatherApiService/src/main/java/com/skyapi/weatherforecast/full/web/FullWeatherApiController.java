package com.skyapi.weatherforecast.full.web;

import com.skyapi.weatherforecast.common.Location;
import com.skyapi.weatherforecast.full.service.FullWeatherService;
import com.skyapi.weatherforecast.full.web.dto.FullWeatherDTO;
import com.skyapi.weatherforecast.hourly_weather.web.exception.BadRequestException;
import com.skyapi.weatherforecast.location.exceptions.GeoLocationException;
import com.skyapi.weatherforecast.location.service.GeoLocationService;
import com.skyapi.weatherforecast.utility.CommonUtility;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/full")
public class FullWeatherApiController {

    private final GeoLocationService locationService;
    private final FullWeatherService weatherService;
    private final ModelMapper modelMapper;


    public FullWeatherApiController(GeoLocationService locationService,
                                    FullWeatherService weatherService,
                                    ModelMapper modelMapper) {
        this.locationService = locationService;
        this.weatherService = weatherService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public ResponseEntity <?> getFullWeatherByIPAddress(HttpServletRequest request) throws GeoLocationException {

        String ipAddress = CommonUtility.getIPAddress (request);
        Location locationFromIp = locationService.getLocation (ipAddress);
        Location locationInDB = weatherService.getByLocation (locationFromIp);

        if (locationInDB == null){
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok ()
                .contentType (MediaType.APPLICATION_JSON)
                .body (entity2Dto (locationInDB));
    }

    @GetMapping("/{locationCode}")
    public ResponseEntity <?> getFullWeatherByLocationCode(@PathVariable("locationCode") String locationCode){

        Location locationInDB = weatherService.get (locationCode);

        return ResponseEntity.ok (entity2Dto (locationInDB));
    }

    @PutMapping("/{locationCode}")
    public ResponseEntity<?> updateFullWeather (@PathVariable String locationCode,
                                                @RequestBody @Valid FullWeatherDTO dto) throws BadRequestException {

        if (dto.getListHourlyWeather ().isEmpty ()){
            throw new BadRequestException ("Hourly weather data cannot be empty!");
        }
        if (dto.getListDailyWeather ().isEmpty ()){
            throw new BadRequestException ("Daily weather data cannot be empty!");
        }

        return null;
    }



    private FullWeatherDTO entity2Dto(Location entity){
        FullWeatherDTO dto = modelMapper.map (entity, FullWeatherDTO.class);

        return dto;
    }
}
