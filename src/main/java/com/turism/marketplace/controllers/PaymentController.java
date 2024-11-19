package com.turism.marketplace.controllers;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.turism.marketplace.models.Payment;
import com.turism.marketplace.services.PaymentService;

@Controller
public class PaymentController {
    private final PaymentService paymentService;
    private final HttpServletRequest request;

    public PaymentController(PaymentService paymentService, HttpServletRequest request) {
        this.paymentService = paymentService;
        this.request = request;
    }

    @QueryMapping
    public List<Payment> findAllMyPayments() {
        String preferredUsername = request.getHeader("X-Preferred-Username");
        return paymentService.findAllPaymentsByUser(preferredUsername);
    }

    @QueryMapping
    public Payment getMyShoppingCart() {
        String preferredUsername = request.getHeader("X-Preferred-Username");
        return paymentService.findShoppingCartByUser(preferredUsername);
    }

    @MutationMapping
    public String pay() {
        String preferredUsername = request.getHeader("X-Preferred-Username");
        paymentService.pay(preferredUsername);
        return "Payment registered";
    }

    @MutationMapping
    public String addToMyShoppingCart(@Argument String serviceId) {
        String preferredUsername = request.getHeader("X-Preferred-Username");
        try {
            paymentService.addToShoppingCart(preferredUsername, serviceId);
        } catch (IllegalArgumentException e) {
            return e.getMessage();
        }

        return "Service added";
    }

    @MutationMapping
    public String removeFromMyShoppingCart(@Argument String serviceId) {
        String preferredUsername = request.getHeader("X-Preferred-Username");
        try {
            paymentService.removeFromShoppingCart(preferredUsername, serviceId);
        } catch (IllegalArgumentException e) {
            return e.getMessage();
        }

        return "Service removed";
    }
}
