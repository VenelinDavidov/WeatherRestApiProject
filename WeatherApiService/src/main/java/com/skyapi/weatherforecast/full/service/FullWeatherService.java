package com.skyapi.weatherforecast.full.service;

import com.skyapi.weatherforecast.common.Location;
import com.skyapi.weatherforecast.location.exceptions.LocationNotFoundException;
import com.skyapi.weatherforecast.location.repository.LocationRepository;
import org.springframework.stereotype.Service;

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
}
