package rest.client.examples.hourly.update;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import rest.client.examples.hourly.HourlyWeather;

public class UpdateHourlyWeatherAsObject {

	public static void main(String[] args) {

		String requestURI = "http://localhost:8080/v1/hourly/{code}";
		String locationCode = "NYC_USA";

		RestTemplate restTemplate = new RestTemplate();

		// create objects
		HourlyWeather forecast1 = new HourlyWeather();
		forecast1.setHourOfDay(10);
		forecast1.setTemperature(20);
		forecast1.setPrecipitation(44);
		forecast1.setStatus("Rain");

		HourlyWeather forecast2 = new HourlyWeather();
		forecast2.setHourOfDay(11);
		forecast2.setTemperature(22);
		forecast2.setPrecipitation(55);
		forecast2.setStatus("Rain");

		HourlyWeather forecast3 = new HourlyWeather();
		forecast3.setHourOfDay(12);
		forecast3.setTemperature(24);
		forecast3.setPrecipitation(22);
		forecast3.setStatus("Sunny");

		HourlyWeather forecast4 = new HourlyWeather();
		forecast4.setHourOfDay(13);
		forecast4.setTemperature(30);
		forecast4.setPrecipitation(65);
		forecast4.setStatus("Sunny");

		HourlyWeather[] hourlyForecast = new HourlyWeather[] { forecast1, forecast2, forecast3, forecast4 };

		var request = new HttpEntity<Object>(hourlyForecast);

		var response = restTemplate.exchange(
				requestURI, HttpMethod.PUT, request, Object.class, locationCode);

		HttpStatusCode statusCode = response.getStatusCode();
		System.out.println("Status code: " + statusCode);

		if (statusCode.value() == HttpStatus.OK.value()) {
			 System.out.println("Hourly weather data updated.");
			 System.out.println(response.getBody());
		}
	}
}
