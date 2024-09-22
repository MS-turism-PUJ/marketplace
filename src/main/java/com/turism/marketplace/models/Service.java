package com.turism.marketplace.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table  (name = "services")
public class Service {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false)
    private String serviceId;
    @Column(nullable = false)
    private Float price;
    @Column(nullable = false)
    private String name;
    private String description;
    @Column(nullable = false)
    private String serviceCategory;
    
    private Double Latitude;
    
    private Double Longitude;
    
    private Date departureDate;
    
    private Date arrivalDate;

    private String transportType;
    @ManyToOne
    @JoinColumn(name = "content_id")
    private Content category;
    @OneToOne
    @JoinColumn(name = "payment_id")
    private Payment content;
}
