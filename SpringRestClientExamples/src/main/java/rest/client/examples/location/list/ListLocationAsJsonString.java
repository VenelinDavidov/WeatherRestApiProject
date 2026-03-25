package rest.client.examples.location.list;

import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

public class ListLocationAsJsonString {

	public static void main(String[] args) {

		String requestURI = "http://localhost:8080/v1/locations";

		RestTemplate restTamplate = new RestTemplate();

		try {
			String response = restTamplate.getForObject(requestURI, String.class);
			System.out.println(response);
		} catch (RestClientResponseException ex) {
			HttpStatusCode statusCode = ex.getStatusCode();
			System.out.println("Http status cede: " +  statusCode);
			ex.printStackTrace();
		}
	}

}
