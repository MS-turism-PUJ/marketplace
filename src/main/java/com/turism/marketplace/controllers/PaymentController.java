package com.turism.marketplace.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import com.turism.marketplace.models.Payment;
import com.turism.marketplace.models.User;
import com.turism.marketplace.repositories.PaymentRepository;
import com.turism.marketplace.repositories.UserRepository;

public class PaymentController {
    @Autowired
    private PaymentRepository paymentRepository;
    private UserRepository userRepository;

    @QueryMapping
    public List<Payment> findAllByUser(@RequestHeader("X-Preferred-Username") String preferredUsername) {
        System.out.println("User: " + preferredUsername);
        User user = userRepository.findByUsername(preferredUsername);
        return paymentRepository.findByUser(user);
    }
}
