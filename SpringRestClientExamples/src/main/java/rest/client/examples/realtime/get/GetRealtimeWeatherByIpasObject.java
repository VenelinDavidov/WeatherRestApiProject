package rest.client.examples.realtime.get;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

public class GetRealtimeWeatherByIpasObject {

	public static void main(String[] args) {
		
		String requestURI = "http://localhost:8080/v1/realtime";
		
		RestTemplate restTemplate = new RestTemplate();
		
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		
		String clientIpAddress = "108.30.178.78";
		httpHeaders.add("X_FORWARED_FOR", clientIpAddress);
		
		HttpEntity<String> request = new HttpEntity<>(httpHeaders);
		var responseEntity = restTemplate.exchange(requestURI, HttpMethod.GET, request, String.class);
		
		if(responseEntity.getStatusCode().is2xxSuccessful()) {
			String body = responseEntity.getBody();
			System.out.println(body);
		}
	}

}
