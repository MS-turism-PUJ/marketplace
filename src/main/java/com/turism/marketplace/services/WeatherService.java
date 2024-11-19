package com.turism.marketplace.services;

import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.turism.marketplace.dtos.WeatherDTO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WeatherService {

    private final String apiKey = "fdc1185eb2308a1bb3545c8aef23f7f4";
    private final String baseUrl = "https://api.openweathermap.org/data/2.5/weather?units=metric&appid=" + apiKey;

    public WeatherDTO getWeatherData(String city) {
        RestTemplate restTemplate = new RestTemplate();
        String url = baseUrl + "&q=" + city;

        try {
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);

            // Crear una instancia de WeatherData
            WeatherDTO weatherData = new WeatherDTO();
            weatherData.setCity((String) response.get("name"));
            weatherData.setCountry((String) ((Map) response.get("sys")).get("country"));
            weatherData.setTemperature((Double) ((Map) response.get("main")).get("temp"));
            weatherData.setTempMax((Double) ((Map) response.get("main")).get("temp_max"));
            weatherData.setTempMin((Double) ((Map) response.get("main")).get("temp_min"));
            weatherData.setHumidity((Integer) ((Map) response.get("main")).get("humidity"));
            weatherData.setPressure((Integer) ((Map) response.get("main")).get("pressure"));
            weatherData.setClouds((Integer) ((Map) response.get("clouds")).get("all"));
            weatherData.setWindSpeed((Double) ((Map) response.get("wind")).get("speed"));

            // Manejo explícito de la lista de weather
            List<Map<String, Object>> weatherList = (List<Map<String, Object>>) response.get("weather");
            if (weatherList != null && !weatherList.isEmpty()) {
                Map<String, Object> weatherDetails = weatherList.get(0);
                weatherData.setDescription((String) weatherDetails.get("description"));
                weatherData.setIcon((String) weatherDetails.get("icon"));
            }

            return weatherData;
        } catch (HttpClientErrorException.NotFound e) {
            // Manejo de excepción para ciudad no encontrada (404)
            System.out.println("City not found: {}" + city);
            return createEmptyWeatherData(city);
        } catch (Exception e) {
            // Manejo de cualquier otra excepción
            System.out.println("Error fetching weather data for city {}: {}" + city + e.getMessage());
            return createEmptyWeatherData(city);
        }
    }

    private WeatherDTO createEmptyWeatherData(String city) {
        // Crear un objeto con valores nulos
        WeatherDTO emptyData = new WeatherDTO();
        emptyData.setCity(city);
        emptyData.setCountry("");
        emptyData.setTemperature(0);
        emptyData.setTempMax(0);
        emptyData.setTempMin(0);
        emptyData.setHumidity(0);
        emptyData.setPressure(0);
        emptyData.setClouds(0);
        emptyData.setWindSpeed(0);
        emptyData.setDescription("City not found");
        emptyData.setIcon(null);
        return emptyData;
    }
}
