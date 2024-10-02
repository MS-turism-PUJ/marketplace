package com.turism.marketplace.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.Set;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "services")
public class Service {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String serviceId;

    @Column(nullable = false)
    private Float price;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false)
    private String serviceCategory;

    private Double latitude;

    private Double longitude;

    private Date departureDate;

    private Date arrivalDate;

    private String transportType;

    @Column(nullable = false)
    private String country;

    @Column(nullable = false)
    private String city;

    public Service(String serviceId) {
        this.serviceId = serviceId;
    }
}
