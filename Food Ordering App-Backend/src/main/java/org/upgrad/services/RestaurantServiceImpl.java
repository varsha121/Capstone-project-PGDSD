package org.upgrad.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.upgrad.models.Category;
import org.upgrad.models.Restaurant;
import org.upgrad.repositories.RestaurantRepository;
import org.upgrad.requestResponseEntity.CategoryResponse;
import org.upgrad.requestResponseEntity.RestaurantResponse;
import org.upgrad.requestResponseEntity.RestaurantResponseCategorySet;

import java.util.*;

/** author: Mohan
 * This interface implementation contains the list of all the service methods implementation
 */
@Service
public class RestaurantServiceImpl implements RestaurantService {

    @Autowired
    private RestaurantRepository restaurantRepository;

    private List<RestaurantResponse> restaurantResponseList;

    private List<Restaurant> restaurants;

    @Override
    public List<RestaurantResponse> getAllRestaurant() {
        restaurants = restaurantRepository.findAllRestaurant();
        this.getRestaurantResponseList(restaurants);
        return restaurantResponseList;
    }

    @Override
    public List<RestaurantResponse> getRestaurantByName(String restaurantName) {
        restaurants = restaurantRepository.findRestaurantByName(restaurantName);
        this.getRestaurantResponseList(restaurants);
        return restaurantResponseList;
    }

    @Override
    public List<RestaurantResponse> getRestaurantByCategory(String categoryName) {
        restaurants = restaurantRepository.findRestaurantByCategory(categoryName);
        this.getRestaurantResponseList(restaurants);
        return restaurantResponseList;
    }

    @Override
    public RestaurantResponseCategorySet getRestaurantDetails(int id) {
        RestaurantResponseCategorySet restaurantResponseCategorySet = null;
        Restaurant restaurant = restaurantRepository.findRestaurantById(id);
        System.out.print(restaurant);
        if (restaurant != null) {
            Set<CategoryResponse> categorySet = new LinkedHashSet<>();
            List<Category> categories = restaurant.getCategories();
            categories.sort(Comparator.comparing(Category::getCategoryName));
            categories.forEach(category -> {
                CategoryResponse categoryResponse = new CategoryResponse(category.getId(), category.getCategoryName(),
                        category.getItems());
                categorySet.add(categoryResponse);
            });

            restaurantResponseCategorySet = new RestaurantResponseCategorySet(restaurant.getId(), restaurant.getRestaurantName(),
                    restaurant.getPhotoUrl(), restaurant.getUserRating(), restaurant.getAvgPrice(), restaurant.getNumberUsersRated(),
                    restaurant.getAddress(), categorySet);
        }

        return restaurantResponseCategorySet;
    }

    @Override
    public Restaurant updateRating(int rating, int id) {
        Restaurant restaurant;
        restaurant = restaurantRepository.findRestaurantById(id);
        int count = restaurantRepository.updateRating(rating, restaurant.getNumberUsersRated() + 1, id);
        System.out.println("count" + count);
        if (count > 0) {
            restaurant = restaurantRepository.findRestaurantById(id);
        }
        return restaurant;
    }


    private void getRestaurantResponseList(List<Restaurant> restaurants) {

        restaurantResponseList = new ArrayList<>();
        restaurants.forEach(restaurant -> {
            StringBuilder categories = new StringBuilder();
            int count = 0;
            restaurant.getCategories().sort(Comparator.comparing(Category::getCategoryName));
            for (Category category :
                    restaurant.getCategories()) {
                if (count++ < restaurant.getCategories().size() - 1) {
                    categories.append(category.getCategoryName()).append(", ");
                } else {
                    categories.append(category.getCategoryName());
                }
            }

            RestaurantResponse res = new RestaurantResponse(restaurant.getId(), restaurant.getRestaurantName(),
                    restaurant.getPhotoUrl(), restaurant.getUserRating(), restaurant.getAvgPrice(),
                    restaurant.getNumberUsersRated(), restaurant.getAddress(), categories.toString());
            restaurantResponseList.add(res);
        });
    }
}
