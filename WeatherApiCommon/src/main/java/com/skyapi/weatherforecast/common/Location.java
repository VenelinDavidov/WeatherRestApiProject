package com.skyapi.weatherforecast.common;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "locations")
public class Location {

	@Column(nullable = false, unique = true, length = 12)
	@Id
	@NotBlank
	private String code;

	@Column(nullable = false, length = 125)
	@JsonProperty("city_name")
	@NotBlank
	private String cityName;

	@Column(length = 128)
	@JsonProperty("region_name")
	@NotBlank
	private String regionName;

	@Column(length = 64, nullable = false)
	@JsonProperty("country_name")
	@NotBlank
	private String countryName;

	@Column(length = 2, nullable = false)
	@JsonProperty("country_code")
	@NotBlank
	private String countryCode;

	
	private boolean enabled;
	@JsonIgnore
	private boolean trashed;
	
	
	
	@Override
	public int hashCode() {
		return Objects.hash(code);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Location other = (Location) obj;
		return Objects.equals(code, other.code);
	}
	
	

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isTrashed() {
		return trashed;
	}

	public void setTrashed(boolean trashed) {
		this.trashed = trashed;
	}

}
