package org.upgrad.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.upgrad.models.Coupon;
import org.upgrad.models.Item;
import org.upgrad.models.Order;
import org.upgrad.repositories.AddressRepository;
import org.upgrad.repositories.CouponRepository;
import org.upgrad.repositories.ItemRepository;
import org.upgrad.repositories.OrderRepository;
import org.upgrad.requestResponseEntity.ItemQuantity;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {


    private CouponRepository couponRepository;

    private OrderRepository orderRepository;

    private AddressRepository addressRepository;

    private ItemRepository itemRepository;

    public OrderServiceImpl(CouponRepository couponRepository, OrderRepository orderRepository,
                            AddressRepository addressRepository, ItemRepository itemRepository) {

        this.couponRepository = couponRepository;

        this.orderRepository = orderRepository;

        this.addressRepository = addressRepository;

        this.itemRepository = itemRepository;

    }

    @Override
    public Coupon getCoupon(String couponName) {
        return couponRepository.findCouponByName(couponName);
    }

    @Override
    public List<Order> getOrdersByUser(Integer userId) {
        return orderRepository.findOrdersByUser(userId);
    }

    @Override
    public Integer addOrderWithPermAddress(Integer addressId, Integer paymentId, Integer userId, ArrayList<ItemQuantity> itemQuantities, double bill, Integer couponId, double discount) {
        //needs item price and details so that we can map with orderitem

        if (couponId == null) couponId = 1;
        orderRepository.saveOrder(bill, couponId, discount, paymentId, userId, addressId);
        Integer orderId = orderRepository.findlastInsertedRecord();

        itemQuantities.forEach(itemQuantity -> {
            Item item = itemRepository.getItemById(itemQuantity.getItemId());
            double totalprice = item.getPrice() * itemQuantity.getQuantity();
            orderRepository.saveOrderItem(orderId, itemQuantity.getItemId(), itemQuantity.getQuantity(), totalprice);
        });
        return orderId;
    }

    @Override
    public Integer addOrder(String flatBuilNo, String locality, String city, String zipcode,
                            int stateId, String type, int paymentId, Integer userId,
                            List<ItemQuantity> itemQuantities, double bill, Integer couponId, double discount) {

        addressRepository.addAddress(flatBuilNo, locality, city, zipcode, stateId);
        Integer addressId = addressRepository.findIdForLatestAddress();
        if (type == null || type.equals("")) {
            type = "temp";
        }
        addressRepository.userAddressMapping(type, userId, addressId);
        if (couponId == null) couponId = 1;
        orderRepository.saveOrder(bill, couponId, discount, paymentId, userId, addressId);
        Integer orderId = orderRepository.findlastInsertedRecord();

        itemQuantities.forEach(itemQuantity -> {
            Item item = itemRepository.getItemById(itemQuantity.getItemId());
            double totalprice = item.getPrice() * itemQuantity.getQuantity();
            orderRepository.saveOrderItem(orderId, itemQuantity.getItemId(), itemQuantity.getQuantity(), totalprice);
        });

        return orderId;
    }


}
