package org.upgrad.services;

import org.springframework.stereotype.Service;
import org.upgrad.models.Category;
import org.upgrad.repositories.CategoryRepository;

import javax.transaction.Transactional;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Iterable<Category> getAllCategories() {
        return categoryRepository.getCategories();
    }

    @Override
    public Category getCategory(String categoryName) {
        return categoryRepository.getCategoryByName(categoryName);
    }

    @Override
    public Integer getCategoryCount(String categoryName) {


        return categoryRepository.getCategoryCountByName(categoryName);
    }
}
