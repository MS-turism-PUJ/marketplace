package com.turism.marketplace.repositories;

import com.turism.marketplace.models.Service;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceRepository extends JpaRepository<Service, String> {
}