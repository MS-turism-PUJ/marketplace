package com.turism.marketplace.controllers;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

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
    public List<Content> findContentsByFilter(@Argument String filter, @Argument Integer page, @Argument Integer limit) {
        return contentService.findByFilter(filter, page, limit);
    }
}
