package com.turism.marketplace.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import com.turism.marketplace.repositories.PaymentRepository;

public class PaymentController {
    @Autowired
    private PaymentRepository paymentRepository;

    @GetMapping("/allPayments")
    public String getAllPayments(@RequestHeader("X-Preferred-Username") String preferredUsername) {
        System.out.println("User: " + preferredUsername);
        return paymentRepository.findAll().toString();
    }
}
