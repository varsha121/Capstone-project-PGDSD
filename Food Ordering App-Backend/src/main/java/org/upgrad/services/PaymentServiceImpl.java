package org.upgrad.services;

import org.springframework.stereotype.Service;
import org.upgrad.models.Payment;
import org.upgrad.repositories.PaymentRepository;

import java.util.List;

@Service
public class PaymentServiceImpl implements PaymentService {

    private PaymentRepository paymentRepository;

    public PaymentServiceImpl(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    public List<Payment> getPaymentMethods() {
        return paymentRepository.findPaymentMethod();
    }
}
