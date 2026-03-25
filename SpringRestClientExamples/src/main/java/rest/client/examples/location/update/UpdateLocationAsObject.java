package rest.client.examples.location.update;


import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import rest.client.examples.location.Location;

public class UpdateLocationAsObject {
	public static void main (String[] args) {
		
		String requestURI = "http://localhost:8080/v1/locations";
		
		RestTemplate  restTamplate = new RestTemplate();
		
		Location location = new Location();
		location.setCode("BNGL_IN");
		location.setCityName("Bangalone");
		location.setRegionName("Karnataka_Region");
		location.setCountryCode("IN");
		location.setCountryName("India");
		
		
		HttpEntity <Location> request = new HttpEntity<Location>(location);
		
		ResponseEntity<Location> response = restTamplate.exchange(requestURI, HttpMethod.PUT, request, Location.class);
		
		if(response.getStatusCode().is2xxSuccessful()) {
			Location updatedLocation= response.getBody();
			System.out.println(updatedLocation);
		}
	}
}
