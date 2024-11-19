package com.turism.marketplace.controllers;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.turism.marketplace.dtos.ServiceFilterDTO;
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
    public List<Service> findServicesByFilter(@Argument ServiceFilterDTO filter, @Argument Integer page,
            @Argument Integer limit) {

        return serviceService.findByFilter(filter.getFilter(), filter.getMoreThan(), filter.getLessThan(), filter.getCategories(), page, limit);
    }
}
