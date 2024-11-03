package com.turism.marketplace.controllers;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.turism.marketplace.models.Service;
import com.turism.marketplace.services.ServiceService;

import java.util.List;
import java.util.Optional;

@Controller
public class ServiceController {
    private final ServiceService serviceService;

    public ServiceController(ServiceService serviceService) {
        this.serviceService = serviceService;
    }

    @QueryMapping
    public List<Service> findAllServices(@Argument Integer page, @Argument Integer limit) {
        return serviceService.findAll(page, limit);
    }

    @QueryMapping
    public Optional<Service> findServiceById(@Argument String serviceId) {
        return serviceService.findById(serviceId);
    }

    @QueryMapping
    public List<Service> findServicesByFilter(@Argument Object filter, @Argument Integer page, @Argument Integer limit) {
        System.out.println(filter);
        System.out.println(page);
        System.out.println(limit);
        return List.of();
    }

    // @QueryMapping
    // public List<Service> servicesByCategory(@Argument String serviceCategory) {
    //     return serviceService.findByCategory(serviceCategory);
    // }

    // @QueryMapping
    // public List<Service> servicesByWord(@Argument String word) {
    //     return Stream.of(
    //             serviceRepository.findByDescriptionContaining(word),
    //             serviceRepository.findByNameContaining(word),
    //             serviceRepository.findByCountryContaining(word),
    //             serviceRepository.findByCityContaining(word))
    //             .flatMap(List::stream)
    //             .distinct()
    //             .collect(Collectors.toList());
    // }
}
