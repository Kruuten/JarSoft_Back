package com.kruten.jarsofttesttask.repository;

import com.kruten.jarsofttesttask.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRep extends JpaRepository<Category, Integer> {
    List<Category> findAllByNameContainingIgnoreCase(String name);
    Category findCategoryByName(String name);
    Category findCategoryByReqName(String name);
    boolean existsCategoryByName(String name);
    boolean existsCategoryByReqName(String name);
    boolean existsCategoryByNameAndIdNotLike(String name, int id);
    boolean existsCategoryByReqNameAndIdNotLike(String name, int id);

}
