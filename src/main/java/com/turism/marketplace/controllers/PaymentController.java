package com.turism.marketplace.controllers;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.turism.marketplace.models.Payment;
import com.turism.marketplace.models.User;
import com.turism.marketplace.repositories.PaymentRepository;
import com.turism.marketplace.repositories.UserRepository;

@Controller
public class PaymentController {
    private PaymentRepository paymentRepository;
    private UserRepository userRepository;
    private HttpServletRequest request;

    @Autowired
    public PaymentController(PaymentRepository paymentRepository, UserRepository userRepository, HttpServletRequest request) {
        this.paymentRepository = paymentRepository;
        this.userRepository = userRepository;
        this.request = request;
    }

    @QueryMapping
    public List<Payment> findAllByUser() {
        String preferredUsername = request.getHeader("X-Preferred-Username");
        User user = userRepository.findByUsername(preferredUsername);
        return paymentRepository.findByUser(user);
    }
}
