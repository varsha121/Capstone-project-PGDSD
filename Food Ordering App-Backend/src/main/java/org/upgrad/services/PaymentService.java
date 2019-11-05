package org.upgrad.services;

import org.upgrad.models.Payment;

import java.util.List;

/** author: Mohan
 * This PaymentService interface has the Services in the Payment Service Implementation
 */
public interface PaymentService {
    List<Payment> getPaymentMethods();
}
