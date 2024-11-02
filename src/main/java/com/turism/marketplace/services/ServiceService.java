package com.turism.marketplace.services;

import com.turism.marketplace.repositories.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.util.List;

@org.springframework.stereotype.Service
public class ServiceService {

    @Autowired
    private ServiceRepository serviceRepository;

    public List<com.turism.marketplace.models.Service> findAllServices(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return serviceRepository.findAll(pageable).getContent();
    }

    public void createService(com.turism.marketplace.models.Service service) {
        serviceRepository.save(service);
    }
}

