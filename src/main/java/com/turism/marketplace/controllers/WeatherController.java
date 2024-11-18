package com.turism.marketplace.controllers;

import java.util.Map;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.turism.marketplace.services.WeatherService;

@Controller
public class WeatherController {

    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    /**
     * GraphQL Query: Obtener datos del clima por ciudad.
     *
     * @param city Nombre de la ciudad
     * @return Datos procesados del clima
     */
    @QueryMapping
    public Map<String, Object> getWeather(@Argument String city) {
        return weatherService.getWeatherData(city);
    }
}
