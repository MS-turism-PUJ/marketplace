package com.turism.marketplace.repositories;

import com.turism.marketplace.models.Content;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContentRepository extends JpaRepository<Content, String> {
    List<Content> findByNameContainingOrDescriptionContaining(String name, String description, Pageable pageable);
}
