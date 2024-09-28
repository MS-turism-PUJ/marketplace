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

    // Relación Many-to-Many con Payment
    @ManyToMany
    @JoinTable(
        name = "payment_has_services", // Nombre de la tabla intermedia
        joinColumns = @JoinColumn(name = "service_id"), // Columna que representa esta entidad en la tabla intermedia
        inverseJoinColumns = @JoinColumn(name = "payment_id") // Columna que representa la otra entidad
    )
    private Set<Payment> payments;
}
