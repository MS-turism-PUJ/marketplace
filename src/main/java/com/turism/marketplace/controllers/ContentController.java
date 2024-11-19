package com.turism.marketplace.controllers;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.turism.marketplace.dtos.ServiceFilterDTO;
import com.turism.marketplace.models.Content;
import com.turism.marketplace.services.ContentService;

import java.util.List;
import java.util.Optional;

@Controller
public class ContentController {
    private final ContentService contentService;

    public ContentController(ContentService contentService) {
        this.contentService = contentService;
    }

    @QueryMapping
    public List<Content> findAllContents(@Argument Integer page, @Argument Integer limit) {
        return contentService.findAll(page, limit);
    }

    @QueryMapping
    public Optional<Content> findContentById(@Argument String contentId) {
        return contentService.findById(contentId);
    }

    @QueryMapping
    public List<Content> findContentsByFilter(@Argument ServiceFilterDTO filter, @Argument Integer page,
            @Argument Integer limit) {
        Float minPrice;
        Float maxPrice;
        if (filter.getPrice() == null) {
            minPrice = null;
            maxPrice = null;
        } else {
            minPrice = filter.getPrice().getMoreThan();
            maxPrice = filter.getPrice().getLessThan();
        }
        return contentService.findByFilter(filter.getFilter(), minPrice, maxPrice, filter.getCategories(), page, limit);
    }
}
