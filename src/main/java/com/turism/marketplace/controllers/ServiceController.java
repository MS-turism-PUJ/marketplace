package com.turism.marketplace.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.turism.marketplace.models.Service;
import com.turism.marketplace.repositories.ServiceRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
public class ServiceController {

    @Autowired
    private ServiceRepository serviceRepository;

    @QueryMapping
    public List<Service> allServices() {
        return serviceRepository.findAll();
    }

    @QueryMapping
    public Optional<Service> serviceById(@Argument String serviceId) {
        return serviceRepository.findById(serviceId);
    }

    @QueryMapping
    public List<Service> servicesByCategory(@Argument String serviceCategory) {
        return serviceRepository.findByServiceCategory(serviceCategory);
    }

    @QueryMapping
    public List<Service> servicesByWord(@Argument String word) {
        return Stream.of(
                serviceRepository.findByDescriptionContaining(word),
                serviceRepository.findByNameContaining(word),
                serviceRepository.findByCountryContaining(word),
                serviceRepository.findByCityContaining(word))
                .flatMap(List::stream)
                .distinct()
                .collect(Collectors.toList());
    }

    @MutationMapping
    public Service createService(@Argument Float price, @Argument String name, @Argument String description,
            @Argument String serviceCategory) {
        Service service = new Service(null, price, name, description, serviceCategory, null, null, null, null, null,
                null, null);
        return serviceRepository.save(service);
    }

    @MutationMapping
    public Service updateService(@Argument String serviceId, @Argument Float price, @Argument String name,
            @Argument String description, @Argument String serviceCategory) {
        return serviceRepository.findById(serviceId).map(service -> {
            service.setPrice(price);
            service.setName(name);
            service.setDescription(description);
            service.setServiceCategory(serviceCategory);
            return serviceRepository.save(service);
        }).orElseThrow(() -> new RuntimeException("Service not found"));
    }

    @MutationMapping
    public Boolean deleteService(@Argument String serviceId) {
        serviceRepository.deleteById(serviceId);
        return true;
    }
}
