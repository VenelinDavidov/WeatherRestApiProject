package rest.client.examples.location.add;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class AddLocationAsJsonString {

	public static void main(String[] args) {
		
		String requestURL ="http://localhost:8080/v1/locations";
		
		
		RestTemplate  restTamplate = new RestTemplate();
		
		String json = """
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
		
		ResponseEntity<String> responseEntity = restTamplate.postForEntity(requestURL, request, String.class);
		
		HttpStatusCode httpStatusCode = responseEntity.getStatusCode();
		System.out.println("Response status code is: " + httpStatusCode);
		
		String addedLocationJson = responseEntity.getBody();
		
		System.out.println(addedLocationJson);
	}

}
