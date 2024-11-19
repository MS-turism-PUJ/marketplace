package com.turism.marketplace.services;

import com.turism.marketplace.models.Content;
import com.turism.marketplace.models.ServiceCategory;
import com.turism.marketplace.repositories.ContentRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ContentService {
    private ContentRepository contentRepository;

    public ContentService(ContentRepository contentRepository) {
        this.contentRepository = contentRepository;
    }

    public List<Content> findAll(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return contentRepository.findAll(pageable).getContent();
    }

    public List<Content> findByFilter(String filter, Float minPrice, Float maxPrice, List<ServiceCategory> categories,
            Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return contentRepository.searchByFilter(filter, minPrice, maxPrice,
                categories.stream().map(el -> el.toString()).toList(), pageable).getContent();
    }

    public Optional<Content> findById(String contentId) {
        return contentRepository.findById(contentId);
    }

    public void createContent(Content content) {
        contentRepository.save(content);
    }
}
