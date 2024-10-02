package com.turism.marketplace.controllers;

import java.util.List;
import java.util.Optional;

import com.turism.marketplace.models.Service;
import com.turism.marketplace.repositories.ServiceRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.turism.marketplace.models.Payment;
import com.turism.marketplace.models.User;
import com.turism.marketplace.repositories.PaymentRepository;
import com.turism.marketplace.repositories.UserRepository;

@Controller
public class PaymentController {
    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;
    private final ServiceRepository serviceRepository;
    private final HttpServletRequest request;

    @Autowired
    public PaymentController(PaymentRepository paymentRepository, UserRepository userRepository, ServiceRepository serviceRepository, HttpServletRequest request) {
        this.paymentRepository = paymentRepository;
        this.userRepository = userRepository;
        this.serviceRepository = serviceRepository;
        this.request = request;
    }

    @QueryMapping
    public List<Payment> findAllByUser() {
        String preferredUsername = request.getHeader("X-Preferred-Username");
        User user = userRepository.findByUsername(preferredUsername);
        return paymentRepository.findByUserAndPaidTrue(user);
    }

    @QueryMapping
    public Payment getShoppingCart() {
        String preferredUsername = request.getHeader("X-Preferred-Username");
        User user = userRepository.findByUsername(preferredUsername);
        return paymentRepository.findByUserAndPaidFalse(user);
    }

    @MutationMapping
    public String pay() {
        String preferredUsername = request.getHeader("X-Preferred-Username");
        System.out.println(preferredUsername);
        User user = userRepository.findByUsername(preferredUsername);
        System.out.println(user);
        Payment shoppingCart = paymentRepository.findByUserAndPaidFalse(user);
        System.out.println(shoppingCart);
        shoppingCart.setPaid(true);
        System.out.println(shoppingCart);
        paymentRepository.save(shoppingCart);
        System.out.println(shoppingCart);
        return "Payment registered";
    }

    @MutationMapping
    public String addToShoppingCart(@Argument String serviceId) {
        String preferredUsername = request.getHeader("X-Preferred-Username");
        User user = userRepository.findByUsername(preferredUsername);
        Payment shoppingCart = paymentRepository.findByUserAndPaidFalse(user);
        Optional<Service> optionalService = serviceRepository.findById(serviceId);
        if (optionalService.isEmpty()) {
            return "Service not found";
        }
        if (shoppingCart == null) {
            shoppingCart = new Payment(0F, user);
        }

        shoppingCart.setTotalAmount(shoppingCart.getTotalAmount() + optionalService.get().getPrice());
        shoppingCart.getServices().add(optionalService.get());
        paymentRepository.save(shoppingCart);

        return "Service added";
    }
}
