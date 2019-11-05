package org.upgrad.services;

import org.upgrad.models.Category;

/**
 * This CategoryService interface contanis the list of all thee services in the Category Service implementation class.
 */
public interface CategoryService {

    Iterable<Category> getAllCategories();

    Category getCategory(String categoryName);

    Integer getCategoryCount(String categoryName);

}
