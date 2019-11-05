package org.upgrad.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.upgrad.models.Coupon;
import org.upgrad.models.Order;
import org.upgrad.requestResponseEntity.ItemQuantity;
import org.upgrad.services.OrderService;
import org.upgrad.services.UserAuthTokenService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserAuthTokenService userAuthTokenService;

    @GetMapping("/coupon/{couponName}")
    @CrossOrigin
    public ResponseEntity<?> getCouponByName(@PathVariable("couponName") String couponName,
                                             @RequestHeader("accessToken") String accessToken) {
        if (userAuthTokenService.isUserLoggedIn(accessToken) == null) {
            return new ResponseEntity<>("Please Login first to access this endpoint!", HttpStatus.UNAUTHORIZED);
        } else if (userAuthTokenService.isUserLoggedIn(accessToken).getLogoutAt() != null) {
            return new ResponseEntity<>("You have already logged out. Please Login first to access this endpoint!", HttpStatus.UNAUTHORIZED);
        } else {
            Coupon coupon = orderService.getCoupon(couponName);
            if (coupon == null) {
                return new ResponseEntity<>("Invalid Coupon!", HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>(coupon, HttpStatus.OK);
            }

        }
    }

    @GetMapping("")
    @CrossOrigin
    public ResponseEntity<?> getOrdersByUser(@RequestHeader("accessToken") String accessToken) {

        if (userAuthTokenService.isUserLoggedIn(accessToken) == null) {
            return new ResponseEntity<>("Please Login first to access this endpoint!", HttpStatus.UNAUTHORIZED);
        } else if (userAuthTokenService.isUserLoggedIn(accessToken).getLogoutAt() != null) {
            return new ResponseEntity<>("You have already logged out. Please Login first to access this endpoint!", HttpStatus.UNAUTHORIZED);
        } else {
            Integer userId = userAuthTokenService.getUserId(accessToken);
            List<Order> orders = orderService.getOrdersByUser(userId);
            if (orders == null || orders.size() == 0) {
                return new ResponseEntity<>("No orders have been made yet!", HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>(orders, HttpStatus.OK);
            }

        }

    }

    @PostMapping("")
    @CrossOrigin
    public ResponseEntity<?> addOrder(@RequestParam(value = "addressId", required = false) Integer addressId,
                                      @RequestParam(value = "flatBuilNo", required = false) String flatBuilNo,
                                      @RequestParam(value = "locality", required = false) String locality,
                                      @RequestParam(value = "city", required = false) String city,
                                      @RequestParam(value = "zipcode", required = false) String zipcode,
                                      @RequestParam(value = "stateId", required = false) Integer stateId,
                                      @RequestParam(value = "type", required = false) String type,
                                      @RequestParam("paymentId") Integer paymentId,
                                      @RequestBody ArrayList<ItemQuantity> itemQuantities,
                                      @RequestParam("bill") Double bill,
                                      @RequestParam(value = "couponId", required = false) Integer couponId,
                                      @RequestParam(value = "discount", defaultValue = "0.0") Double discount,
                                      @RequestHeader("accessToken") String accessToken) {
        if (userAuthTokenService.isUserLoggedIn(accessToken) == null) {
            return new ResponseEntity<>("Please Login first to access this endpoint!", HttpStatus.UNAUTHORIZED);
        } else if (userAuthTokenService.isUserLoggedIn(accessToken).getLogoutAt() != null) {
            return new ResponseEntity<>("You have already logged out. Please Login first to access this endpoint!", HttpStatus.UNAUTHORIZED);
        } else {
            Integer userId = userAuthTokenService.getUserId(accessToken);
            Integer orderId;
            if (null != addressId) {
                orderId = orderService.addOrderWithPermAddress(addressId, paymentId, userId, itemQuantities, bill, couponId, discount);
            } else {
                if (zipcode == null || !zipcode.matches("^[1-9][0-9]{5}$")) {
                    return new ResponseEntity<>("Invalid zipcode!", HttpStatus.BAD_REQUEST);
                }
                orderId = orderService.addOrder(flatBuilNo, locality, city, zipcode, stateId, type,
                        paymentId, userId, itemQuantities, bill, couponId, discount);

            }
            return new ResponseEntity<>(orderId, HttpStatus.OK);
        }
    }
}
