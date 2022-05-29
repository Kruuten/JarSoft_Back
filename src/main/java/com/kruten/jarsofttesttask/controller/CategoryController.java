package com.kruten.jarsofttesttask.controller;

import com.kruten.jarsofttesttask.entity.Category;
import com.kruten.jarsofttesttask.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/category")
    public ResponseEntity<List<Category>> getCategories(@RequestParam(required = false) String name) {
        return categoryService.getCategories(name);
    }

    @GetMapping("/category/{id}")
    public ResponseEntity<Category> getCategory(@PathVariable int id) {
        return categoryService.getCategory(id);
    }

    @PutMapping("category/{id}")
    public ResponseEntity<Category> editCategory(@RequestBody @Valid Category categoryDetails, @PathVariable int id) {
        return categoryService.editCategory(categoryDetails, id);
    }

    @PostMapping("/category")
    public ResponseEntity<Category> addNewCategory(@RequestBody @Valid Category category) {
        return categoryService.createNewCategory(category);
    }

    @DeleteMapping("/category/{id}")
    public ResponseEntity<Category> deleteCategory(@PathVariable int id) {
        return categoryService.deleteCategory(id);
    }
}
