package org.upgrad.services;

import org.upgrad.models.Item;

import java.util.List;

/**
 * This ItemService interface has the list of all services in the Item service implementation class.
 */
public interface ItemService {

    List<Item> getItemByPopularity(int restaurantId);

    Item getItemById(int id);

}
