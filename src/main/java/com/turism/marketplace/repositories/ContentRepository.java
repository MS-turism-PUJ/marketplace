package com.turism.marketplace.repositories;

import com.turism.marketplace.models.Content;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContentRepository extends JpaRepository<Content, String> {
}
