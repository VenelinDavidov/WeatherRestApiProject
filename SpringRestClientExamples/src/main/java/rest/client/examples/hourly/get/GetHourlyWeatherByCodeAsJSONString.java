package rest.client.examples.hourly.get;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

public class GetHourlyWeatherByCodeAsJSONString {

	public static void main(String[] args) {
		
		String requestURI = "http://localhost:8080/v1/hourly/NYC_USA";
		
		RestTemplate restTemplate = new RestTemplate();
		
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		
		httpHeaders.add("X-CURRENT-HOUR", "20");
		
	    var request = new HttpEntity<String>( httpHeaders);
	    
	    var response = restTemplate.exchange(requestURI, HttpMethod.GET, request, String.class);
	    
	    HttpStatusCode statusCode = response.getStatusCode();
	    
	    System.out.println("Status code is: " + statusCode);
	    
	    
	    
	    if(statusCode.value() == HttpStatus.NO_CONTENT.value()) {
	    	
	    	System.out.println("No forecast hourly data available!");
	    	
	    } else if(statusCode.value() == HttpStatus.OK.value()) {
	    	
	    	String body = response.getBody();
	    	System.out.println(body);
	    	
	    } else if(statusCode.value() == HttpStatus.NOT_FOUND.value()) {
	    	
	    	System.out.println("No managed location found for the given location code!");
	    }
	}
}
