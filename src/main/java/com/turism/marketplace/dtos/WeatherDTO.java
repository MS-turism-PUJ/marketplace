package com.turism.marketplace.dtos;

public class WeatherDTO {
    private String city;
    private String country;
    private double temperature;
    private double tempMax;
    private double tempMin;
    private int humidity;
    private int pressure;
    private int clouds;
    private double windSpeed;
    private String description;
    private String icon;

    // Constructor vacío
    public WeatherDTO() {
    }

    // Constructor completo
    public WeatherDTO(String city, String country, double temperature, double tempMax, double tempMin,
            int humidity, int pressure, int clouds, double windSpeed, String description, String icon) {
        this.city = city;
        this.country = country;
        this.temperature = temperature;
        this.tempMax = tempMax;
        this.tempMin = tempMin;
        this.humidity = humidity;
        this.pressure = pressure;
        this.clouds = clouds;
        this.windSpeed = windSpeed;
        this.description = description;
        this.icon = icon;
    }

    // Getters y Setters
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getTempMax() {
        return tempMax;
    }

    public void setTempMax(double tempMax) {
        this.tempMax = tempMax;
    }

    public double getTempMin() {
        return tempMin;
    }

    public void setTempMin(double tempMin) {
        this.tempMin = tempMin;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public int getPressure() {
        return pressure;
    }

    public void setPressure(int pressure) {
        this.pressure = pressure;
    }

    public int getClouds() {
        return clouds;
    }

    public void setClouds(int clouds) {
        this.clouds = clouds;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    @Override
    public String toString() {
        return "WeatherData{" +
                "city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", temperature=" + temperature +
                ", tempMax=" + tempMax +
                ", tempMin=" + tempMin +
                ", humidity=" + humidity +
                ", pressure=" + pressure +
                ", clouds=" + clouds +
                ", windSpeed=" + windSpeed +
                ", description='" + description + '\'' +
                ", icon='" + icon + '\'' +
                '}';
    }
}
