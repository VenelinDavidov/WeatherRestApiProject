package com.skyapi.weatherforecast.location.service;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ip2location.IP2Location;
import com.ip2location.IPResult;
import com.skyapi.weatherforecast.common.Location;
import com.skyapi.weatherforecast.location.exceptions.GeoLocationException;

@Service
public class GeoLocationService {

	private static final Logger LOGGER = LoggerFactory.getLogger(GeoLocationService.class);
	private String DBPath = "ip2locdb/IP2LOCATION-LITE-DB3.BIN";
	private IP2Location ipLocator = new IP2Location();

	public GeoLocationService() {

		try {
			ipLocator.Open(DBPath);
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
		}
	}
	
	 

	public Location getLocation(String ipAddress) throws GeoLocationException {

		try {
			IPResult result = ipLocator.IPQuery(ipAddress);

			if (!"OK".equals(result.getStatus())) {
				throw new GeoLocationException("GeoLocation failed with status:" + result.getStatus());
			}

			return new Location(result.getCity(), result.getRegion(), result.getCountryLong(),result.getCountryShort());

		} catch (IOException e) {
			throw new GeoLocationException("Error quering IP Database", e);
		}

	}

}
