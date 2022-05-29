package com.kruten.jarsofttesttask.service;

import com.kruten.jarsofttesttask.entity.Banner;
import com.kruten.jarsofttesttask.entity.Category;
import com.kruten.jarsofttesttask.repository.CategoryRep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    @Autowired
    CategoryRep categoryRep;

    public ResponseEntity<List<Category>> getCategories(String name){
        List<Category> categories = new ArrayList<>();

        if (name != null)
            categoryRep.findAllByNameContainingIgnoreCase(name)
                    .stream()
                    .filter(category -> !category.isDeleted())
                    .forEach(categories::add);

        else categoryRep.findAll()
                .stream()
                .filter(category -> !category.isDeleted())
                .forEach(categories::add);

        if (categories.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    public ResponseEntity<Category> getCategory(int id){
        Optional<Category> optional = categoryRep.findById(id);
        if (optional.isPresent()){
            return new ResponseEntity<>(optional.get(), HttpStatus.OK);
        } else return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    public ResponseEntity<Category> editCategory(Category categoryDetails, int id){
        Optional<Category> optional = categoryRep.findById(id);
        boolean existWithName = categoryRep.existsCategoryByNameAndIdNotLike(categoryDetails.getName(), id);
        boolean existWithReqName = categoryRep.existsCategoryByReqNameAndIdNotLike(categoryDetails.getReqName(), id);

        if (optional.isPresent()) {
            if (existWithName)
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            else if (existWithReqName)
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            else{
                Category category = optional.get();
                category.setName(categoryDetails.getName());
                category.setReqName(category.getReqName());
                return new ResponseEntity<>(categoryRep.save(category), HttpStatus.OK);
            }
        } else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<Category> createNewCategory(Category category){
        boolean existWithName = categoryRep.existsCategoryByName(category.getName());
        boolean existWithReqName = categoryRep.existsCategoryByReqName(category.getReqName());

        if (existWithName)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        else if (existWithReqName)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        else return new ResponseEntity<>(categoryRep
                    .save(new Category(category.getName()
                            , category.getReqName()))
                    , HttpStatus.OK);
    }

    public ResponseEntity<Category> deleteCategory(int id){
        Optional<Category> optional = categoryRep.findById(id);
        if (optional.isPresent()){
            Category category = optional.get();
            List<Banner> banners = new ArrayList<>();
            category.getBanners().stream().filter(banner -> !banner.isDeleted()).forEach(banners::add);

            if (banners.isEmpty()) {
                category.setDeleted(true);
                return new ResponseEntity<>(categoryRep.save(category), HttpStatus.OK);
            } else{
                return new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED);
            }
        }
        else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
