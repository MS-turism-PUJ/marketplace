package com.turism.marketplace.dtos;

import java.io.Serializable;
import java.time.LocalTime;

import com.turism.marketplace.models.Service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ServiceMessageDTO implements Serializable {
    private String serviceId;

    private Float price;

    private String name;

    private String city;

    private String country;

    private String description;

    private Double latitude;

    private Double longitude;

    private Double arrivalLatitude;

    private Double arrivalLongitude;

    private String departureDate;

    private String time;

    private String transportType;

    private String drink;

    private String lunch;

    private String dessert;

    private String category;

    private String userId;

    public ServiceMessageDTO(Service service) {
        this.serviceId = service.getServiceId();
        this.price = service.getPrice();
        this.name = service.getName();
        this.city = service.getCity();
        this.country = service.getCountry();
        this.description = service.getDescription();
        this.latitude = service.getLatitude();
        this.longitude = service.getLongitude();
        this.arrivalLatitude = service.getArrivalLatitude();
        this.arrivalLongitude = service.getArrivalLongitude();
        this.departureDate = service.getDepartureDate() == null ? null : service.getDepartureDate().toString();
        this.time = service.getTime() == null ? null : service.getTime().toString();
        this.transportType = service.getTransportType();
        this.drink = service.getDrink();
        this.lunch = service.getLunch();
        this.dessert = service.getDessert();
        this.category = service.getCategory();
        this.userId = service.getUser().getUserId();
    }

    public Service toService() {
        Service service = new Service();
        service.setServiceId(this.serviceId);
        service.setPrice(this.price);
        service.setName(this.name);
        service.setCity(this.city);
        service.setCountry(this.country);
        service.setDescription(this.description);
        service.setLatitude(this.latitude);
        service.setLongitude(this.longitude);
        service.setArrivalLatitude(this.arrivalLatitude);
        service.setArrivalLongitude(this.arrivalLongitude);
        service.setDepartureDate(this.departureDate == null ? null : java.sql.Date.valueOf(this.departureDate));
        service.setTime(this.time == null ? null : LocalTime.parse(this.time));
        service.setTransportType(this.transportType);
        service.setDrink(this.drink);
        service.setLunch(this.lunch);
        service.setDessert(this.dessert);
        service.setCategory(this.category);
        return service;
    }
}
