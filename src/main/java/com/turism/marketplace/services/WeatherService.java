package com.turism.marketplace.services;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WeatherService {

    private final String apiKey = "fdc1185eb2308a1bb3545c8aef23f7f4";
    private final String baseUrl = "https://api.openweathermap.org/data/2.5/weather?units=metric&appid=" + apiKey;

    public Map<String, Object> getWeatherData(String city) {
        RestTemplate restTemplate = new RestTemplate();
        String url = baseUrl + "&q=" + city;

        try {
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);

            // Procesar datos para enviar solo la información necesaria al frontend
            Map<String, Object> processedData = new HashMap<>();
            processedData.put("city", response.get("name"));
            processedData.put("country", ((Map) response.get("sys")).get("country"));
            processedData.put("temperature", ((Map) response.get("main")).get("temp"));
            processedData.put("temp_max", ((Map) response.get("main")).get("temp_max"));
            processedData.put("temp_min", ((Map) response.get("main")).get("temp_min"));
            processedData.put("humidity", ((Map) response.get("main")).get("humidity"));
            processedData.put("pressure", ((Map) response.get("main")).get("pressure"));
            processedData.put("clouds", ((Map) response.get("clouds")).get("all"));
            processedData.put("wind_speed", ((Map) response.get("wind")).get("speed"));

            // Manejo explícito de la lista de weather
            List<Map<String, Object>> weatherList = (List<Map<String, Object>>) response.get("weather");
            if (weatherList != null && !weatherList.isEmpty()) {
                Map<String, Object> weatherDetails = weatherList.get(0);
                processedData.put("description", weatherDetails.get("description"));
                processedData.put("icon", weatherDetails.get("icon"));
            }

            return processedData;
        } catch (Exception e) {
            throw new RuntimeException("Error fetching weather data: " + e.getMessage());
        }
    }
}
