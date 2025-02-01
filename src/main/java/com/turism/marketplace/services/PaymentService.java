package com.turism.marketplace.services;

import com.turism.marketplace.models.Payment;
import com.turism.marketplace.models.Service;
import com.turism.marketplace.models.User;
import com.turism.marketplace.repositories.PaymentRepository;
import com.turism.marketplace.repositories.ServiceRepository;
import com.turism.marketplace.repositories.UserRepository;

import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Service
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;
    private final ServiceRepository serviceRepository;

    public PaymentService(PaymentRepository paymentRepository, UserRepository userRepository, ServiceRepository serviceRepository) {
        this.paymentRepository = paymentRepository;
        this.userRepository = userRepository;
        this.serviceRepository = serviceRepository;
    }

    public List<Payment> findAllPaymentsByUser(String username) {
        User user = userRepository.findByUsername(username);
        return paymentRepository.findByUserAndPaidTrue(user);
    }

    public Payment findShoppingCartByUser(String username) {
        User user = userRepository.findByUsername(username);
        return paymentRepository.findByUserAndPaidFalse(user);
    }

    public void pay(String username) {
        User user = userRepository.findByUsername(username);
        Payment shoppingCart = paymentRepository.findByUserAndPaidFalse(user);
        shoppingCart.setPaid(true);
        paymentRepository.save(shoppingCart);
    }

    public void addToShoppingCart(String username, String serviceId) {
        User user = userRepository.findByUsername(username);
        Payment shoppingCart = paymentRepository.findByUserAndPaidFalse(user);

        Optional<Service> optionalService = serviceRepository.findById(serviceId);
        if (optionalService.isEmpty()) {
            throw new IllegalArgumentException("Service not found");
        }
        if (shoppingCart == null) {
            shoppingCart = new Payment(0F, user);
        }

        Integer lenght = shoppingCart.getServices().size();
        System.out.println(shoppingCart);
        shoppingCart.getServices().add(optionalService.get());

        if (lenght.equals(shoppingCart.getServices().size())) {
            throw new IllegalArgumentException("Service already added");
        }

        shoppingCart.setTotalAmount(shoppingCart.getTotalAmount() + optionalService.get().getPrice());
        paymentRepository.save(shoppingCart);
    }

    public void removeFromShoppingCart(String username, String serviceId) {
        User user = userRepository.findByUsername(username);
        Payment shoppingCart = paymentRepository.findByUserAndPaidFalse(user);
        Optional<Service> optionalService = serviceRepository.findById(serviceId);
        if (optionalService.isEmpty()) {
            throw new IllegalArgumentException("Service not found");
        }
        if (shoppingCart == null) {
            shoppingCart = new Payment(0F, user);
        }

        Integer length = shoppingCart.getServices().size();
        shoppingCart.getServices().remove(optionalService.get());
        if (length.equals(shoppingCart.getServices().size())) {
            throw new IllegalArgumentException("Service not present");
        }
        shoppingCart.setTotalAmount(shoppingCart.getTotalAmount() - optionalService.get().getPrice());
        paymentRepository.save(shoppingCart);
    }
}
