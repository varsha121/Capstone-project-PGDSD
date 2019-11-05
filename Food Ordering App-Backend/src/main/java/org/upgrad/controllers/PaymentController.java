package org.upgrad.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.upgrad.models.Payment;
import org.upgrad.services.PaymentService;
import org.upgrad.services.UserAuthTokenService;

import java.util.List;

@RestController
@RequestMapping("/payment")
public class PaymentController {
    @Autowired
    private UserAuthTokenService userAuthTokenService;

    @Autowired
    private PaymentService paymentService;

    /**
     * this api fetches the alltypes of payment methods 
     */
    @GetMapping("")
    @CrossOrigin
    public ResponseEntity<?> paymentMethod(@RequestHeader String accessToken) {
        if (userAuthTokenService.isUserLoggedIn(accessToken) == null) {
            return new ResponseEntity<>("Please Login first to access this endpoint!", HttpStatus.UNAUTHORIZED);
        } else if (userAuthTokenService.isUserLoggedIn(accessToken).getLogoutAt() != null) {
            return new ResponseEntity<>("You have already logged out. Please Login first to access this endpoint!", HttpStatus.UNAUTHORIZED);
        } else {
            List<Payment> payments = paymentService.getPaymentMethods();
            return new ResponseEntity<>(payments, HttpStatus.OK);
        }
    }

}
