package com.turism.marketplace.repositories;

import com.turism.marketplace.models.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, String> {
}
