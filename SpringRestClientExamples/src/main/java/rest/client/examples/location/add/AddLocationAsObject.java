package rest.client.examples.location.add;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import rest.client.examples.location.Location;

public class AddLocationAsObject {

	public static void main(String[] args) {
		
		String requestURL ="http://localhost:8080/v1/locations";
		
		
		RestTemplate  restTamplate = new RestTemplate();
		
		Location newLocation = new Location();
		newLocation.setCode("BNGL_IN");
		newLocation.setCityName("Bangalone");
		newLocation.setRegionName("Karnataka");
		newLocation.setCountryCode("IN");
		newLocation.setCountryName("India");
		
		
		
		HttpEntity <Location> request = new HttpEntity<Location>(newLocation);
		
		ResponseEntity<Location> responseEntity = restTamplate.postForEntity(requestURL, request, Location.class);
		
		HttpStatusCode httpStatusCode = responseEntity.getStatusCode();
		System.out.println("Response status code is: " + httpStatusCode);
		
		Location addedLocation = responseEntity.getBody();
		
		System.out.println(addedLocation);
	}

}
