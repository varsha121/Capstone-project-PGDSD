package org.upgrad.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.upgrad.models.Item;
import org.upgrad.requestResponseEntity.RestaurantResponseCategorySet;
import org.upgrad.services.ItemService;
import org.upgrad.services.RestaurantService;

import java.util.List;

@RestController
@RequestMapping("/item/restaurant")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @Autowired
    private RestaurantService restaurantService;

    @GetMapping("/{restaurantId}")
    @CrossOrigin
    public ResponseEntity<?> getItemById(@PathVariable int restaurantId) {

        RestaurantResponseCategorySet sets = restaurantService.getRestaurantDetails(restaurantId);
        if (sets != null) {
            List<Item> item = itemService.getItemByPopularity(restaurantId);
            return new ResponseEntity<>(item, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("No Restaurant by this id!", HttpStatus.BAD_REQUEST);
        }
    }

}
