package rest.client.examples.location.delete;
import java.util.*;

import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;


public class DeleteLocationSimple {

	public static void main(String[] args) {
		
		String requestURI = "http://localhost:8080/v1/locations/{code}";
		
	    Map<String, String> params = new HashMap<>();
	    params.put("code", "NYC_MI");
	    
		RestTemplate  restTamplate = new RestTemplate();
	    
		try {
		    restTamplate.delete(requestURI, params);
		    System.out.println("Location is deleted.");
		}catch(RestClientResponseException ex) {
			System.out.println("Error status code is: " + ex);
			ex.printStackTrace();
		}
	}

}
