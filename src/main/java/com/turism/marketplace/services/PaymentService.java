package com.turism.marketplace.services;

import com.turism.marketplace.models.Payment;
import com.turism.marketplace.repositories.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository PaymentRepository;



    
}
