package com.turism.marketplace.services;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

@Service
public class CountryApiService {

    private final RestTemplate restTemplate;
    private final Gson gson;

    public CountryApiService(RestTemplate restTemplate, Gson gson) {
        this.restTemplate = restTemplate;
        this.gson = gson;
    }

    public CountryInfo getCountryInfo(String countryName) {
        // Llamada a la API REST Countries
        String url = "https://restcountries.com/v3.1/name/" + countryName;
        String jsonResponse = restTemplate.getForObject(url, String.class);

        // Parsear la respuesta JSON usando Gson
        Type listType = new TypeToken<List<CountryInfo>>() {
        }.getType();
        List<CountryInfo> countries = gson.fromJson(jsonResponse, listType);

        // Devolver el primer país (en caso de múltiples resultados)
        return (countries != null && !countries.isEmpty()) ? countries.get(0) : null;
    }

    // Modelo para representar la información relevante del país
    public static class CountryInfo {
        private Name name;
        private List<String> capital;
        private Map<String, Currency> currencies;
        private String region;
        private Map<String, String> languages;
        private List<Double> latlng;
        private int population;

        // Métodos para extraer los datos relevantes
        public String getCommonName() {
            return name != null ? name.getCommon() : null;
        }

        public String getOfficialName() {
            return name != null ? name.getOfficial() : null;
        }

        public String getCapital() {
            return (capital != null && !capital.isEmpty()) ? capital.get(0) : null;
        }

        public String getCurrency() {
            return (currencies != null && !currencies.isEmpty())
                    ? currencies.values().iterator().next().getName()
                    : null;
        }

        public String getRegion() {
            return region;
        }

        public String getFirstLanguage() {
            return (languages != null && !languages.isEmpty())
                    ? languages.values().iterator().next()
                    : null;
        }

        public Double getLatitude() {
            return (latlng != null && latlng.size() > 0) ? latlng.get(0) : null;
        }

        public Double getLongitude() {
            return (latlng != null && latlng.size() > 1) ? latlng.get(1) : null;
        }

        public int getPopulation() {
            return population;
        }

        // Clase para representar el campo `name`
        public static class Name {
            private String common;
            private String official;

            public String getCommon() {
                return common;
            }

            public String getOfficial() {
                return official;
            }
        }

        // Clase para representar una moneda
        public static class Currency {
            private String name;
            private String symbol;

            public String getName() {
                return name;
            }

            public String getSymbol() {
                return symbol;
            }
        }
    }
}
