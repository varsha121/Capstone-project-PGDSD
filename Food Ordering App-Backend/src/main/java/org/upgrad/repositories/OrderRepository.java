package org.upgrad.repositories;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.upgrad.models.Order;

import java.util.List;

@Repository
public interface OrderRepository extends CrudRepository<Order, Integer> {

    @Query(nativeQuery = true, value = "SELECT * FROM orders WHERE user_id = ?1 ORDER BY date DESC")
    List<Order> findOrdersByUser(Integer userId);

    @Query(nativeQuery = true, value = "select id from orders order by id desc limit 1")
    Integer findlastInsertedRecord();

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "INSERT into orders (bill, coupon_id, discount," +
            " \"date\", payment_id, user_id, address_id)\n" +
            "VALUES(?1, ?2, ?3, now(), ?4, ?5, ?6)")
    Integer saveOrder(double bill, int couponId, double discount, Integer paymentId, Integer userId, Integer addressId);


    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "INSERT INTO public.order_item" +
            "(order_id, item_id, quantity, price)" +
            "VALUES(?1, ?2, ?3, ?4);")
    void saveOrderItem(Integer orderId, Integer itemId, Integer quantity, Double price);
}
