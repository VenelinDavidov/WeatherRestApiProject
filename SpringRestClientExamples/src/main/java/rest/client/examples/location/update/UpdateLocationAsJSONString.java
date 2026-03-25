package rest.client.examples.location.update;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

public class UpdateLocationAsJSONString {
	public static void main (String[] args) {
		
		String requestURI = "http://localhost:8080/v1/locations";
		

		RestTemplate restTamplate = new RestTemplate();
		
		String json ="""
				{
				     "code": "MADRID_MD",
				     "city_name": "Madrid",
				     "region_name": "Community of Madrid",
				     "country_code": "ES",
				     "country_name": "Spain",
				     "enabled": true
				 }
				""";
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		
		HttpEntity <String> request = new HttpEntity<String>(json, headers);
		
		try {
		   restTamplate.put(requestURI, request, String.class);
		   System.out.println("Location is updated.");
		}catch(RestClientResponseException ex) {
			System.out.println("Error status code is: " + ex.getStatusCode());
			ex.printStackTrace();
		}
	}
}
