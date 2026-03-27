package rest.client.examples.hourly.get;

import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import rest.client.examples.hourly.HourlyForecastDTO;
import rest.client.examples.hourly.HourlyWeather;

public class GetHourlyWeatherByCodeIPASObject {

	public static void main(String[] args) {
		
		String requestURI = "http://localhost:8080/v1/hourly/NYC_USA";
		
		RestTemplate restTemplate = new RestTemplate();
		
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		
		httpHeaders.add("X-CURRENT-HOUR", "10");
		httpHeaders.add("X_FORWARED_FOR", "108.30.178.78");
		
	    var request = new HttpEntity<HourlyForecastDTO>( httpHeaders);
	    
	    var response = restTemplate.exchange(requestURI, HttpMethod.GET, request, HourlyForecastDTO.class);
	    
	    HttpStatusCode statusCode = response.getStatusCode();
	    
	    System.out.println("Status code is: " + statusCode);
	    
	    
	    
	    if(statusCode.value() == HttpStatus.NO_CONTENT.value()) {
	    	
	    	System.out.println("No forecast hourly data available!");
	    	
	    } else if (statusCode.value() == HttpStatus.OK.value()) {
	    	
	    	HourlyForecastDTO dto = response.getBody();	    	 
	    	System.out.println("Location: " +  dto.getLocation());
	    	var hourlyForecast = dto.getHourlyForecast();
	    	hourlyForecast.forEach(System.out::println);
	    }
	}
}
