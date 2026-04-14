package com.skyapi.weatherforecast.base.web;

import com.skyapi.weatherforecast.base.RootEntity;
import com.skyapi.weatherforecast.daily.web.DailyWeatherController;
import com.skyapi.weatherforecast.full.web.FullWeatherApiController;
import com.skyapi.weatherforecast.hourly_weather.web.HourlyWeatherAPIController;
import com.skyapi.weatherforecast.location.exceptions.GeoLocationException;
import com.skyapi.weatherforecast.location.web.LocationApiController;
import com.skyapi.weatherforecast.realtime.web.RealtimeWeatherController;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    @GetMapping("/")
    public ResponseEntity<RootEntity> handleBaseUrI() throws GeoLocationException {
        return ResponseEntity.ok (createRootEntity ());
    }


    private RootEntity createRootEntity() throws GeoLocationException {

        RootEntity entity = new RootEntity ();

        String locationsUrl = WebMvcLinkBuilder
                .linkTo (WebMvcLinkBuilder.methodOn (LocationApiController.class)
                .listLocation ())
                .toString ();
        entity.setLocationsURL (locationsUrl);

        String locationByCodeUrl = WebMvcLinkBuilder
                .linkTo (WebMvcLinkBuilder.methodOn (LocationApiController.class)
                .getLocation (null))
                .toString ();
        entity.setLocationByCodeURL (locationByCodeUrl);


        String realtimeWeatherByIpUrl =  WebMvcLinkBuilder
                .linkTo (WebMvcLinkBuilder.methodOn (RealtimeWeatherController.class)
                .getRealtimeWeatherByIPAddress (null))
                .toString ();
        entity.setRealtimeWeatherByIpURL (realtimeWeatherByIpUrl);

        String realtimeWeatherByCodeUrl =  WebMvcLinkBuilder
                .linkTo (WebMvcLinkBuilder.methodOn (RealtimeWeatherController.class)
                        .getRealtimeWeatherLocationCode (null))
                .toString ();
        entity.setRealtimeByCodeUrl (realtimeWeatherByCodeUrl);


        String hourlyForecastByIpUrl = WebMvcLinkBuilder
                .linkTo (WebMvcLinkBuilder.methodOn (HourlyWeatherAPIController.class)
                .listHourlyForecastByIPAdress (null))
                .toString ();
        entity.setHourlyForecastByIpUrl (hourlyForecastByIpUrl);

        String hourlyForecastByCodeUrl = WebMvcLinkBuilder
                .linkTo (WebMvcLinkBuilder.methodOn (HourlyWeatherAPIController.class)
                .listHourlyWeatherForecastByLocationCode (null, null))
                .toString ();
        entity.setHourlyForecastByCodeUrl (hourlyForecastByCodeUrl);

        String dailyForecastByIpUrl = WebMvcLinkBuilder
                .linkTo (WebMvcLinkBuilder.methodOn (DailyWeatherController.class)
                .listDailyForecastByIPAdress (null))
                .toString ();
        entity.setDailyForecastByIpUrl (dailyForecastByIpUrl);

        String dailyForecastByCodeUrl = WebMvcLinkBuilder
                .linkTo (WebMvcLinkBuilder.methodOn (DailyWeatherController.class)
                .listHourlyForecastByLocationCode (null))
                .toString ();
        entity.setDailyForecastByCodeUrl (dailyForecastByCodeUrl);

        String fullWeatherByIpUrl =  WebMvcLinkBuilder
                .linkTo (WebMvcLinkBuilder.methodOn (FullWeatherApiController.class)
                .getFullWeatherByIPAddress (null))
                .toString ();
        entity.setFullWeatherByIpUrl (fullWeatherByIpUrl);


        String fullWeatherByCodeUrl =  WebMvcLinkBuilder
                .linkTo (WebMvcLinkBuilder.methodOn (FullWeatherApiController.class)
                        .getFullWeatherByLocationCode (null))
                .toString ();
        entity.setFullWeatherByCodeUrl (fullWeatherByCodeUrl);

        return entity;
    }
}
