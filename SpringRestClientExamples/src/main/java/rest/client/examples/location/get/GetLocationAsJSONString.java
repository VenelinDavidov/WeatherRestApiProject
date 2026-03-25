package rest.client.examples.location.get;


import org.springframework.http.HttpStatus;
import java.util.*;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;



public class GetLocationAsJSONString {
	
	public static void main (String[] args) {
		
		String requestURI = "http://localhost:8080/v1/locations/{code}";
		
		Map<String, String> params = new HashMap<>();
		params.put("code", "NYC_MI");
		
		RestTemplate restTamplate = new RestTemplate();
		
		ResponseEntity<String> responseEntity = restTamplate.getForEntity(requestURI, String.class, params);
		
		HttpStatusCode statusCode = responseEntity.getStatusCode();
		System.out.println("Response status code is: " + statusCode);
		
		if(statusCode.value() == HttpStatus.OK.value()) {
			String body = responseEntity.getBody();
			System.out.println(body);
		}
	}

}
