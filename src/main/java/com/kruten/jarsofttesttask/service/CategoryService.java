package com.kruten.jarsofttesttask.service;

import com.kruten.jarsofttesttask.entity.Banner;
import com.kruten.jarsofttesttask.entity.Category;
import com.kruten.jarsofttesttask.repository.CategoryRep;
import com.kruten.jarsofttesttask.validator.ErrorResponse;
import com.kruten.jarsofttesttask.validator.Violation;
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
        return optional.map(category -> new ResponseEntity<>(category, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NO_CONTENT));
    }

    public ResponseEntity<?> editCategory(Category editCategory, int id){
        Optional<Category> optional = categoryRep.findById(id);
        String nameExists = String.format("Category with name [%s] already exist", editCategory.getName());
        String idExists =
                String.format("Category with request name [%s] already exist", editCategory.getReqName());
        boolean existWithName = categoryRep.existsCategoryByNameAndIdNotLike(editCategory.getName(), id);
        boolean existWithReqName = categoryRep.existsCategoryByReqNameAndIdNotLike(editCategory.getReqName(), id);

        if (optional.isPresent()) {
            if (existWithName) {
                ErrorResponse error = new ErrorResponse();
                error.getViolations().add(new Violation("Name", nameExists));
                return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
            }
            else if (existWithReqName) {
                ErrorResponse error = new ErrorResponse();
                error.getViolations().add(new Violation("ID", idExists));
                return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
            }
            else{
                Category category = optional.get();
                category.setName(editCategory.getName());
                category.setReqName(editCategory.getReqName());
                return new ResponseEntity<>(categoryRep.save(category), HttpStatus.OK);
            }
        } else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<?> createNewCategory(Category category){
        String nameExists = String.format("Category with name [%s] already exist", category.getName());
        String idNameExists = String.format("Category with request ID [%s] already exist", category.getReqName());
        boolean existWithName = categoryRep.existsCategoryByName(category.getName());
        boolean existWithIdName = categoryRep.existsCategoryByReqName(category.getReqName());
        ErrorResponse errorResponse = new ErrorResponse();

        if (existWithName) {
            errorResponse.getViolations().add(new Violation("Name", nameExists));
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
        else if (existWithIdName) {
            errorResponse.getViolations().add(new Violation("ID", idNameExists));
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
        else return new ResponseEntity<>(categoryRep
                    .save(new Category(category.getName()
                            , category.getReqName()))
                    , HttpStatus.OK);
    }

    public ResponseEntity<?> deleteCategory(int id){
        Optional<Category> optional = categoryRep.findById(id);
        if (optional.isPresent()){
            Category category = optional.get();
            List<Banner> banners = new ArrayList<>();
            category.getBanners().stream().filter(banner -> !banner.isDeleted()).forEach(banners::add);

            if (banners.isEmpty()) {
                category.setDeleted(true);
                return new ResponseEntity<>(categoryRep.save(category), HttpStatus.OK);
            } else {
              StringBuilder builder = new StringBuilder();
              for (Banner banner: banners) {
                  builder.append("[" + banner.getId() + "]");
              }
                  String error = String.format("Banner(s) with id(s): %s is not deleted, you cannot delete this category", builder);
                  ErrorResponse errorResponse = new ErrorResponse();
                  errorResponse.getViolations().add(new Violation("id", error));
                  return new ResponseEntity<>(errorResponse, HttpStatus.METHOD_NOT_ALLOWED);
            }
        }
        else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
