package org.upgrad.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.upgrad.models.Category;

import java.util.Set;

@Repository
public interface CategoryRepository extends CrudRepository<Category, Integer> {

    @Query(nativeQuery = true, value = "SELECT * FROM CATEGORY order by category_name ASC")
    Iterable<Category> getCategories();

    @Query(nativeQuery = true, value = "SELECT * FROM CATEGORY WHERE ID=?1")
    Category getCategoryById(int id);

    @Query(nativeQuery = true, value = "SELECT * FROM CATEGORY WHERE category_name ~* ?1")
    Category getCategoryByName(String categoryName);

    @Query(nativeQuery = true, value = "SELECT count(*) FROM CATEGORY WHERE CATEGORY_NAME ~* ?1")
    Integer getCategoryCountByName(String categoryName);

    @Query(nativeQuery = true, value = "SELECT CATEGORY_NAME FROM CATEGORY WHERE ID=?1 ORDER BY CATEGORY_NAME ASC")
    String getCategoryNameById(int id);

    @Query(nativeQuery = true, value = "SELECT CATEGORY.* FROM CATEGORY INNER JOIN RESTAURANT_CATEGORY ON CATEGORY.ID=RESTAURANT_CATEGORY.CATEGORY_ID WHERE RESTAURANT_ID=?1 ORDER BY CATEGORY.CATEGORY_NAME ASC")
    Set<Category> getCategoriesByRestId(int id);

}
