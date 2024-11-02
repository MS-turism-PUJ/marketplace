package com.turism.marketplace.repositories;

import com.turism.marketplace.models.Service;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceRepository extends JpaRepository<Service, String> {

    List<Service> findByCategory(String category);

    List<Service> findByDescriptionContaining(String description);

    List<Service> findByNameContaining(String name);

    List<Service> findByCountryContaining(String country);

    List<Service> findByCityContaining(String city);

}
