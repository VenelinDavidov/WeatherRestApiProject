package rest.client.examples.hourly.update;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

public class UpdateHourlyWeatherAsJSONString {

	public static void main(String[] args) {

		String requestURI = "http://localhost:8080/v1/hourly/NYC_USA";

		RestTemplate restTemplate = new RestTemplate();

		String json = """
				[
				  {
				     "hour_of_day": 10,
				     "temperature": 12,
				     "precipitation": 88,
				     "status": "Cloudy"
				   },
				   {
				      "hour_of_day": 11,
				      "temperature": 13,
				      "precipitation": 77,
				      "status": "Cloudy"
				    },
				    {
				      "hour_of_day": 12,
				      "temperature": 15,
				      "precipitation": 66,
				      "status": "Cloudy"
				     },
				     {
				       "hour_of_day": 13,
				       "temperature": 18,
				       "precipitation": 55,
				       "status": "Cloudy"
				       }
				]
				""";

		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);

		var request = new HttpEntity<String>(json, httpHeaders);

		try {
			restTemplate.put(requestURI, request);
			
            System.out.println("Hourly weather data updated.");
		} catch (RestClientResponseException ex) {
			System.out.println("Error Status code: " + ex.getStatusCode());
			ex.printStackTrace();
		}
	}
}
