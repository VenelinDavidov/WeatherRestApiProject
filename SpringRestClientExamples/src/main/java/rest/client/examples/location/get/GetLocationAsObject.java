package rest.client.examples.location.get;


import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import rest.client.examples.location.Location;

public class GetLocationAsObject {
	
	public static void main (String[] args) {
		String requestURI = "http://localhost:8080/v1/locations/MADRID_MD";
		
		RestTemplate restTamplate = new RestTemplate();
		
		ResponseEntity<Location> responseEntity = restTamplate.getForEntity(requestURI, Location.class);
		
		HttpStatusCode statusCode = responseEntity.getStatusCode();
		System.out.println("Response status code is: " + statusCode);
		
		if(statusCode.value() == HttpStatus.OK.value()) {
			Location location = responseEntity.getBody();
			System.out.println(location);
		}
	}

}
