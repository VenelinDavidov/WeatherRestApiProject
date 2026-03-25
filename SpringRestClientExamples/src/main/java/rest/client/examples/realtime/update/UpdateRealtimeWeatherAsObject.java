package rest.client.examples.realtime.update;

import java.util.Date;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import rest.client.examples.realtime.RealtimeWeather;

public class UpdateRealtimeWeatherAsObject {

	public static void main(String[] args) {

		String requestURI = "http://localhost:8080/v1/realtime/{code}";
		String locationCode = "NYC_USA";
		
		RestTemplate restTemplate = new RestTemplate();
		
		RealtimeWeather realtimeWeather =  new RealtimeWeather ();
		realtimeWeather.setTemperature(12);
		realtimeWeather.setHumidity(34);
		realtimeWeather.setPrecipitation(45);
		realtimeWeather.setWindSpeed(33);
		realtimeWeather.setStatus("Rain");
		
		
		var request = new HttpEntity<>(realtimeWeather);
		
		ResponseEntity<RealtimeWeather> responseEntity = 
				restTemplate.exchange(
						              requestURI, 
						              HttpMethod.PUT, 
						              request,
						              RealtimeWeather.class, 
						              locationCode
						              );
		
		if(responseEntity.getStatusCode().is2xxSuccessful()) {
			System.out.println("Succsses Update Realtime Weather!");
			RealtimeWeather body = responseEntity.getBody();
			System.out.println(body);
		}
	}
}
