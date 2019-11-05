package org.upgrad.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.upgrad.models.Restaurant;
import org.upgrad.requestResponseEntity.RestaurantResponse;
import org.upgrad.requestResponseEntity.RestaurantResponseCategorySet;
import org.upgrad.services.RestaurantService;
import org.upgrad.services.UserAuthTokenService;

import java.util.List;

@RestController
@RequestMapping("/restaurant")
public class RestaurantController {

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private UserAuthTokenService userAuthTokenService;

    private List<RestaurantResponse> restaurantResponseList;

    /**
     * This endpoint will retrieve all the restaurants in the order of ratings
     */
    @GetMapping("")
    @CrossOrigin
    public ResponseEntity<?> getAllRestaurant() {

        restaurantResponseList = restaurantService.getAllRestaurant();
        return new ResponseEntity<>(restaurantResponseList, HttpStatus.OK);
    }

    /**
     * This endpoint will retrieve the matched restaurants by Name
     */
    @GetMapping("/name/{reastaurantName}")
    @CrossOrigin
    public ResponseEntity<?> getRestaurantsByName(@PathVariable("reastaurantName") String reastaurantName) {
        restaurantResponseList = restaurantService.getRestaurantByName(reastaurantName);
        if (restaurantResponseList == null || restaurantResponseList.size() == 0)
            return new ResponseEntity<>("No Restaurant by this name!", HttpStatus.NOT_FOUND);
        else
            return new ResponseEntity<>(restaurantResponseList, HttpStatus.OK);
    }

    /**
     * This endpoint will retrieve all the matched Restaurants by Category Name 
     */
    @GetMapping("/category/{categoryName}")
    @CrossOrigin
    public ResponseEntity<?> getResturantsByCategory(@PathVariable("categoryName") String categoryName) {
        restaurantResponseList = restaurantService.getRestaurantByCategory(categoryName);
        if (restaurantResponseList == null || restaurantResponseList.size() == 0)
            return new ResponseEntity<>("No Restaurant under this category!", HttpStatus.NOT_FOUND);
        else {
            return new ResponseEntity<>(restaurantResponseList, HttpStatus.OK);
        }
    }

    @GetMapping("/{restaurantId}")
    @CrossOrigin
    public ResponseEntity<?> getResturantsById(@PathVariable("restaurantId") int restaurantId) {
        RestaurantResponseCategorySet restaurantResponseCategorySet = restaurantService.getRestaurantDetails(restaurantId);

        if (restaurantResponseCategorySet == null)
            return new ResponseEntity<>("No Restaurant by this id!", HttpStatus.NOT_FOUND);
        else
            return new ResponseEntity<>(restaurantResponseCategorySet, HttpStatus.OK);

    }

    /**
     * This endpoint will retrieve the matched restaurants by ID
     */
    @PutMapping("/{restaurantId}")
    @CrossOrigin
    public ResponseEntity<?> updateRestaurant(@PathVariable("restaurantId") int restaurantId,
                                              @RequestParam("rating") String rating,
                                              @RequestHeader String accessToken) {

        if (userAuthTokenService.isUserLoggedIn(accessToken) == null) {
            return new ResponseEntity<>("Please Login first to access this endpoint!", HttpStatus.UNAUTHORIZED);
        } else if (userAuthTokenService.isUserLoggedIn(accessToken).getLogoutAt() != null) {
            return new ResponseEntity<>("You have already logged out. Please Login first to access this endpoint!", HttpStatus.UNAUTHORIZED);
        } else {
            RestaurantResponseCategorySet restaurantResponseCategorySet = restaurantService.getRestaurantDetails(restaurantId);

            if (restaurantResponseCategorySet == null) {
                return new ResponseEntity<>("No Restaurant by this id!", HttpStatus.NOT_FOUND);
            } else {
                Restaurant restaurant = restaurantService.updateRating(Integer.parseInt(rating), restaurantId);
                return new ResponseEntity<>(restaurant, HttpStatus.OK);
            }

        }
    }


}
