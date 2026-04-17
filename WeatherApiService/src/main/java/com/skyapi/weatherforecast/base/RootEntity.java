package com.skyapi.weatherforecast.base;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RootEntity {

    @JsonProperty("locations_url")
    private String locationsURL;

    @JsonProperty("location_by_code_url")
    private  String locationByCodeURL;

    @JsonProperty("realtime_weather_by_ip_url")
    private String realtimeWeatherByIpURL;

    @JsonProperty("realtime_weather_by_code_url")
    private String realtimeByCodeUrl;

    @JsonProperty("hourly_forecast_by_ip_url")
    private String hourlyForecastByIpUrl;

    @JsonProperty("hourly_forecast_by_code_url")
    private String hourlyForecastByCodeUrl;

    @JsonProperty("daily_forecast_by_ip_url")
    private String dailyForecastByIpUrl;

    @JsonProperty("daily_forecast_by_code_url")
    private String dailyForecastByCodeUrl;

    @JsonProperty("full_weather_by_ip_url")
    private String fullWeatherByIpUrl;

    @JsonProperty("full_weather_by_code_url")
    private String fullWeatherByCodeUrl;



    public String getLocationsURL() {
        return locationsURL;
    }

    public void setLocationsURL(String locationsURL) {
        this.locationsURL = locationsURL;
    }

    public String getLocationByCodeURL() {
        return locationByCodeURL;
    }

    public void setLocationByCodeURL(String locationByCodeURL) {
        this.locationByCodeURL = locationByCodeURL;
    }

    public String getRealtimeWeatherByIpURL() {
        return realtimeWeatherByIpURL;
    }

    public void setRealtimeWeatherByIpURL(String realtimeWeatherByIpURL) {
        this.realtimeWeatherByIpURL = realtimeWeatherByIpURL;
    }

    public String getRealtimeByCodeUrl() {
        return realtimeByCodeUrl;
    }

    public void setRealtimeByCodeUrl(String realtimeByCodeUrl) {
        this.realtimeByCodeUrl = realtimeByCodeUrl;
    }

    public String getHourlyForecastByIpUrl() {
        return hourlyForecastByIpUrl;
    }

    public void setHourlyForecastByIpUrl(String hourlyForecastByIpUrl) {
        this.hourlyForecastByIpUrl = hourlyForecastByIpUrl;
    }

    public String getHourlyForecastByCodeUrl() {
        return hourlyForecastByCodeUrl;
    }

    public void setHourlyForecastByCodeUrl(String hourlyForecastByCodeUrl) {
        this.hourlyForecastByCodeUrl = hourlyForecastByCodeUrl;
    }

    public String getDailyForecastByIpUrl() {
        return dailyForecastByIpUrl;
    }

    public void setDailyForecastByIpUrl(String dailyForecastByIpUrl) {
        this.dailyForecastByIpUrl = dailyForecastByIpUrl;
    }

    public String getDailyForecastByCodeUrl() {
        return dailyForecastByCodeUrl;
    }

    public void setDailyForecastByCodeUrl(String dailyForecastByCodeUrl) {
        this.dailyForecastByCodeUrl = dailyForecastByCodeUrl;
    }

    public String getFullWeatherByIpUrl() {
        return fullWeatherByIpUrl;
    }

    public void setFullWeatherByIpUrl(String fullWeatherByIpUrl) {
        this.fullWeatherByIpUrl = fullWeatherByIpUrl;
    }

    public String getFullWeatherByCodeUrl() {
        return fullWeatherByCodeUrl;
    }

    public void setFullWeatherByCodeUrl(String fullWeatherByCodeUrl) {
        this.fullWeatherByCodeUrl = fullWeatherByCodeUrl;
    }
}
