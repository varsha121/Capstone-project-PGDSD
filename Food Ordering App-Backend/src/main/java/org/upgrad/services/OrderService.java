package org.upgrad.services;

import org.upgrad.models.Coupon;
import org.upgrad.models.Order;
import org.upgrad.requestResponseEntity.ItemQuantity;

import java.util.ArrayList;
import java.util.List;

/**
 * This interface has the list of all the services in the Order Service Implementation Class.
 */
public interface OrderService {

    Coupon getCoupon(String couponName);

    List<Order> getOrdersByUser(Integer userId);

    Integer addOrderWithPermAddress(Integer addressId, Integer paymentId, Integer userId, ArrayList<ItemQuantity> itemQuantities,
                                    double bill, Integer couponId, double discount);

    Integer addOrder(String flatBuilNo, String locality, String city, String zipcode, int stateId, String type, int paymentId,
                     Integer userId, List<ItemQuantity> itemQuantities, double bill, Integer couponId, double discount);
}
