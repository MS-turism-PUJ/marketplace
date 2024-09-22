package com.turism.marketplace.repositories;

import com.turism.marketplace.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
}
