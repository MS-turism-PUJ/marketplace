package com.turism.marketplace.repositories;

import com.turism.marketplace.models.Service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ServiceRepository extends JpaRepository<Service, String> {
    @Query("SELECT s FROM Service s WHERE " +
           "(:filter IS NULL OR LOWER(s.description) LIKE LOWER(CONCAT('%', :filter, '%')) OR " +
           "LOWER(s.name) LIKE LOWER(CONCAT('%', :filter, '%')) OR " +
           "LOWER(s.country) LIKE LOWER(CONCAT('%', :filter, '%')) OR " +
           "LOWER(s.city) LIKE LOWER(CONCAT('%', :filter, '%'))) AND " +
           "(:minPrice IS NULL OR s.price >= :minPrice) AND " +
           "(:maxPrice IS NULL OR s.price <= :maxPrice) AND " +
           "(COALESCE(:categories, NULL) IS NULL OR s.category IN :categories)")
    Page<Service> searchByFilter(@Param("filter") String filter,
                                 @Param("minPrice") Float minPrice,
                                 @Param("maxPrice") Float maxPrice,
                                 @Param("categories") List<String> categories,
                                 Pageable pageable);
}
