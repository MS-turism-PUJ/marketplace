package com.turism.marketplace.services;

import com.turism.marketplace.models.Service;
import com.turism.marketplace.models.ServiceCategory;
import com.turism.marketplace.repositories.ServiceRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Service
public class ServiceService {
    private final ServiceRepository serviceRepository;
    private final CountryApiService countryApiService;

    private ServiceService(ServiceRepository serviceRepository, CountryApiService countryApiService) {
        this.serviceRepository = serviceRepository;
        this.countryApiService = countryApiService;
    }

    public List<Service> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return serviceRepository.findAll(pageable).getContent();
    }

    public Optional<Service> findById(String serviceId) {
        return serviceRepository.findById(serviceId);
    }

    public void createService(Service service) {
        CountryApiService.CountryInfo countryInfo = countryApiService.getCountryInfo(service.getCountry());

        if (countryInfo == null) {
            throw new IllegalArgumentException(
                    "No se encontró información para el país especificado: " + service.getCountry());
        }

        // Asignar la información del país al servicio
        service.setLatitude(countryInfo.getLatitude());
        service.setLongitude(countryInfo.getLongitude());
        service.setCountry(countryInfo.getCommonName());
        service.setCapital(countryInfo.getCapital());
        service.setCurrency(countryInfo.getCurrency());
        service.setOfficialName(countryInfo.getOfficialName());
        service.setRegion(countryInfo.getRegion());
        service.setLanguage(countryInfo.getFirstLanguage());
        service.setPopulation(countryInfo.getPopulation());

        serviceRepository.save(service);
    }

    public List<Service> findByFilter(String filter, Float minPrice, Float maxPrice, List<ServiceCategory> categories,
            int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return serviceRepository.searchByFilter(filter, minPrice, maxPrice,
                categories.stream().map(el -> el.toString()).toList(), pageable).getContent();
    }
}
