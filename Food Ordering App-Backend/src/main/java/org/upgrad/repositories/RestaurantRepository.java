package org.upgrad.repositories;


import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.upgrad.models.Restaurant;

import java.util.List;

/**
 * @author mohan
 */
@Repository
public interface RestaurantRepository extends CrudRepository<Restaurant, Integer> {
    @Query(nativeQuery = true, value = "SELECT * FROM restaurant ORDER BY user_rating DESC")
    List<Restaurant> findAllRestaurant();

    @Query(nativeQuery = true, value = "SELECT * FROM RESTAURANT WHERE restaurant_name ILIKE %?1% ORDER BY restaurant_name")
    List<Restaurant> findRestaurantByName(String restaurantName);

    @Query(nativeQuery = true, value = "SELECT * FROM RESTAURANT r inner join restaurant_category rc on r.id = rc.restaurant_id " +
            "inner join category c on  rc.category_id = c.id WHERE c.category_name ~* ?1 ORDER BY r.restaurant_name")
    List<Restaurant> findRestaurantByCategory(String categoryName);

    @Query(nativeQuery = true, value = "SELECT * FROM RESTAURANT WHERE id=?1")
    Restaurant findRestaurantById(int id);

    @Transactional
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(nativeQuery = true, value = "UPDATE RESTAURANT SET user_rating = ?1, number_of_users_rated= ?2 WHERE id=?3")
    int updateRating(int rating, int numUserRated, int id);
}
