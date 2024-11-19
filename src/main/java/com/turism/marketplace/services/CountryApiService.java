package com.turism.marketplace.services;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Type;
import java.util.Collections;
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
        String url = "https://restcountries.com/v3.1/name/" + countryName;

        try {
            // Realizar la llamada a la API
            String jsonResponse = restTemplate.getForObject(url, String.class);

            if (jsonResponse == null || jsonResponse.isEmpty()) {
                return createEmptyCountryInfo();
            }

            // Parsear la respuesta JSON
            Type listType = new TypeToken<List<CountryInfo>>() {
            }.getType();
            List<CountryInfo> countries = gson.fromJson(jsonResponse, listType);

            // Devolver el primer resultado o un objeto vacío si no hay resultados
            return (countries != null && !countries.isEmpty()) ? countries.get(0) : createEmptyCountryInfo();

        } catch (HttpClientErrorException.NotFound e) {
            // Manejar el caso en que el país no se encuentre
            return createEmptyCountryInfo();
        } catch (HttpClientErrorException e) {
            throw new CountryApiException("Error en la solicitud al servidor: " + e.getMessage(), e);
        } catch (JsonSyntaxException e) {
            throw new CountryApiException("Error al parsear la respuesta de la API: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new CountryApiException("Ocurrió un error inesperado: " + e.getMessage(), e);
        }
    }

    // Método para crear un CountryInfo vacío (con valores nulos)
    private CountryInfo createEmptyCountryInfo() {
        CountryInfo emptyCountryInfo = new CountryInfo();
        emptyCountryInfo.name = new CountryInfo.Name();
        emptyCountryInfo.capital = Collections.emptyList();
        emptyCountryInfo.currencies = Collections.emptyMap();
        emptyCountryInfo.region = null;
        emptyCountryInfo.languages = Collections.emptyMap();
        emptyCountryInfo.latlng = Collections.emptyList();
        emptyCountryInfo.population = 0;
        return emptyCountryInfo;
    }

    // Excepción personalizada para la API de Country
    public static class CountryApiException extends RuntimeException {
        public CountryApiException(String message) {
            super(message);
        }

        public CountryApiException(String message, Throwable cause) {
            super(message, cause);
        }
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
