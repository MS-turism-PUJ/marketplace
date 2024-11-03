package com.turism.marketplace.services;

import com.turism.marketplace.models.Service;
import com.turism.marketplace.repositories.ServiceRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Service
public class ServiceService {
    private final ServiceRepository serviceRepository;

    private ServiceService(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    public List<Service> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return serviceRepository.findAll(pageable).getContent();
    }

    public Optional<Service> findById(String serviceId) {
        return serviceRepository.findById(serviceId);
    }

    public void createService(Service service) {
        serviceRepository.save(service);
    }
}

