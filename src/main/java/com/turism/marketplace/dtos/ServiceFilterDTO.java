package com.turism.marketplace.dtos;

import java.util.List;

import com.turism.marketplace.models.ServiceCategory;

import lombok.Data;

@Data
public class ServiceFilterDTO {
    private String filter;
    private List<ServiceCategory> categories;
    private PriceFilter price;

    @Data
    public class PriceFilter {
        private Float lessThan;
        private Float moreThan;
    }
}
