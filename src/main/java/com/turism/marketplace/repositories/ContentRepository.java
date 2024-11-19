package com.turism.marketplace.repositories;

import com.turism.marketplace.models.Content;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ContentRepository extends JpaRepository<Content, String> {
    @Query("SELECT c FROM Content c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :filter, '%')) OR LOWER(c.description) LIKE LOWER(CONCAT('%', :filter, '%'))")
    List<Content> findByFilter(@Param("filter") String filter, Pageable pageable);

    @Query("SELECT c FROM Content c JOIN c.service s WHERE " +
           "(:filter IS NULL OR LOWER(s.description) LIKE LOWER(CONCAT('%', :filter, '%')) OR " +
           "LOWER(c.name) LIKE LOWER(CONCAT('%', :filter, '%')) OR " +
           "LOWER(c.description) LIKE LOWER(CONCAT('%', :filter, '%')) OR " +
           "LOWER(s.description) LIKE LOWER(CONCAT('%', :filter, '%')) OR " +
           "LOWER(s.name) LIKE LOWER(CONCAT('%', :filter, '%')) OR " +
           "LOWER(s.country) LIKE LOWER(CONCAT('%', :filter, '%')) OR " +
           "LOWER(s.city) LIKE LOWER(CONCAT('%', :filter, '%'))) AND " +
           "(:minPrice IS NULL OR s.price >= :minPrice) AND " +
           "(:maxPrice IS NULL OR s.price <= :maxPrice) AND " +
           "(COALESCE(:categories, NULL) IS NULL OR s.category IN :categories)")
    Page<Content> searchByFilter(@Param("filter") String filter,
                                 @Param("minPrice") Float minPrice,
                                 @Param("maxPrice") Float maxPrice,
                                 @Param("categories") List<String> categories,
                                 Pageable pageable);
}
