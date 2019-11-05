package org.upgrad.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.upgrad.models.Item;

import java.util.Set;

@Repository
public interface ItemRepository extends CrudRepository<Item, Integer> {

    @Query(nativeQuery = true, value = "SELECT * from ITEM WHERE ID=?1")
    Item getItemById(int id);

    @Query(nativeQuery = true, value = "SELECT ITEM_ID FROM ORDER_ITEM WHERE ITEM_ID IN(SELECT ITEM_ID FROM RESTAURANT_ITEM WHERE RESTAURANT_ID=?1) GROUP BY ITEM_ID ORDER BY COUNT(*) DESC LIMIT 5")
    Iterable<Integer> getItems(int restaurantId);

    @Query(nativeQuery = true, value = "SELECT * from ITEM INNER JOIN CATEGORY_ITEM on ITEM.id=CATEGORY_ITEM.ITEM_ID WHERE CATEGORY_ID=?1 AND ITEM.id IN(SELECT ITEM.ID from ITEM INNER JOIN RESTAURANT_ITEM on ITEM.id=RESTAURANT_ITEM.ITEM_ID  AND RESTAURANT_ITEM.RESTAURANT_ID=?2)")
    Set<Item> getRestaurantItems(int catId, int restaurantId);

}
