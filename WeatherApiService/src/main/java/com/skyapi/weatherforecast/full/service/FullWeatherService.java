package com.skyapi.weatherforecast.full.service;

import com.skyapi.weatherforecast.common.DailyWeather;
import com.skyapi.weatherforecast.common.HourlyWeather;
import com.skyapi.weatherforecast.common.Location;
import com.skyapi.weatherforecast.common.RealtimeWeather;
import com.skyapi.weatherforecast.location.exceptions.LocationNotFoundException;
import com.skyapi.weatherforecast.location.repository.LocationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FullWeatherService {

    private LocationRepository repo;

    public FullWeatherService(LocationRepository repo) {
        this.repo = repo;
    }

    public Location getByLocation(Location locationFromIp) {

        String cityName = locationFromIp.getCityName ();
        String countryCode = locationFromIp.getCountryCode ();

        Location locationInDB = repo.findByCountryCodeAndCityName (countryCode, cityName);

        if (locationInDB == null) {
            throw new LocationNotFoundException (countryCode, cityName);
        }

        return locationInDB;
    }

    public Location get (String locationCode){
        Location location = repo.findByCode (locationCode);

        if (location == null){
            throw new LocationNotFoundException (locationCode);
        }
        return location;
    }

    public Location update (String locationCode, Location locationInRequest){

        Location locationDB = repo.findByCode (locationCode);

        if (locationDB == null){
            throw new LocationNotFoundException (locationCode);
        }
        RealtimeWeather realtimeWeather = locationInRequest.getRealtimeWeather ();
        realtimeWeather.setLocation (locationDB);

        List <DailyWeather> listDailyWeather = locationInRequest.getListDailyWeather ();
        listDailyWeather.forEach (dw -> dw.getId ().setLocation (locationDB));

        List <HourlyWeather> listHourlyWeather = locationInRequest.getListHourlyWeather ();
        listHourlyWeather.forEach (hw -> hw.getId ().setLocation (locationDB));

        locationInRequest.setCode (locationDB.getCode ());
        locationInRequest.setCityName (locationDB.getCityName ());
        locationInRequest.setRegionName (locationDB.getRegionName ());
        locationInRequest.setCountryCode (locationDB.getCountryCode ());
        locationInRequest.setCountryName (locationDB.getCountryName ());
        locationInRequest.setEnabled (locationDB.isEnabled ());
        locationInRequest.setTrashed (locationDB.isTrashed ());

        return repo.save (locationInRequest);
    }
}
