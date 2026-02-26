package com.skyapi.weatherforecast.utility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.http.HttpServletRequest;

public class CommonUtility {

	private static Logger LOGGER = LoggerFactory.getLogger(CommonUtility.class);
	private static final String X_FORWARED_FOR = "108.30.178.78";
	
	public static String getIPAddress(HttpServletRequest request) {
		
		String ip = request.getHeader("X_FORWARED_FOR");
		
		if(ip == null || ip.isEmpty()) {
			ip = request.getRemoteAddr();
		}
		
		LOGGER.info("Client's IP Adress: " + ip);
		return ip;
	}
}


