package com.turism.marketplace.repositories;

import com.turism.marketplace.models.Payment;
import com.turism.marketplace.models.User;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, String> {

    List<Payment> findByUserAndPaidTrue(User user);
    Payment findByUserAndPaidFalse(User user);
}
