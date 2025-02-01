package com.turism.marketplace.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "payments")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String paymentId;

    @Column(nullable = false)
    private Float totalAmount;

    @Column(nullable = false)
    private Boolean paid;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @ManyToMany
    @JoinTable(name = "payment_has_services", // Nombre de la tabla intermedia
            joinColumns = @JoinColumn(name = "payment_id"), // Columna que representa esta entidad en la tabla
            // intermedia
            inverseJoinColumns = @JoinColumn(name = "service_id") // Columna que representa la otra entidad
    )
    private Set<Service> services;

    public Payment(Float totalAmount, User user) {
        this.totalAmount = totalAmount;
        this.paid = false;
        this.user = user;
        this.services = new HashSet<>();
    }
}
