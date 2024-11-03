package com.turism.marketplace.repositories;

import com.turism.marketplace.models.Content;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ContentRepository extends JpaRepository<Content, String> {
    @Query("SELECT c FROM Content c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :filter, '%')) OR LOWER(c.description) LIKE LOWER(CONCAT('%', :filter, '%'))")
    List<Content> findByFilter(@Param("filter") String filter, Pageable pageable);
}
