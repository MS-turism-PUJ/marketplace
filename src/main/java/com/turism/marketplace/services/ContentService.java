package com.turism.marketplace.services;

import com.turism.marketplace.models.Content;
import com.turism.marketplace.repositories.ContentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContentService {

    @Autowired
    private ContentRepository contentRepository;

    public List<Content> findAll(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return contentRepository.findAll(pageable).getContent();
    }

    public void createContent(Content content) {
        contentRepository.save(content);
    }
}
