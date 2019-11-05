package org.upgrad.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.upgrad.models.Coupon;

@Repository
public interface CouponRepository extends CrudRepository<Coupon, Integer> {

    @Query(nativeQuery = true, value = "SELECT * FROM COUPON  WHERE coupon_name = ?1")
    Coupon findCouponByName(String couponName);
}
