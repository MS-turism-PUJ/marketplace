package com.turism.marketplace.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.turism.marketplace.models.Content;
import com.turism.marketplace.repositories.ContentRepository;

import java.util.List;
import java.util.Optional;

@Controller
public class ContentController {

    @Autowired
    private ContentRepository contentRepository;

    @QueryMapping
    public List<Content> allContents() {
        return contentRepository.findAll();
    }

    @QueryMapping
    public Optional<Content> contentById(@Argument String contentId) {
        return contentRepository.findById(contentId);
    }

}
