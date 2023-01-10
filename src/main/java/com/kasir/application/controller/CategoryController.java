package com.kasir.application.controller;

import com.kasir.application.model.Category;
import com.kasir.application.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://127.0.0.1:5173")
@RequestMapping("/api/category")
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    @GetMapping(path = "/all")
    public ResponseEntity<?> getAllCategoryByUser(Authentication authentication) {
        List<Category> categories = categoryService.getAllCategory(authentication);
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable("id") Long id) throws Exception {
        Category categories = categoryService.getCategoryById(id);
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @PostMapping(path = "/add", consumes = "application/json")
    public ResponseEntity<?> addCategory(@RequestBody Category category, Authentication authentication) throws Exception {
        Category categories = categoryService.addCategory(category, authentication);
        return new ResponseEntity<>(categories, HttpStatus.CREATED);
    }

    @PutMapping(path = "/{id}", consumes = "application/json")
    public ResponseEntity<?> updateCategory(@PathVariable("id") Long id, @RequestBody Category category, Authentication authentication) throws Exception {
        Category categories = categoryService.updateCategory(id, category, authentication);
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable("id") Long id, Authentication authentication) throws Exception {
        Category categories = categoryService.deleteCategory(id, authentication);
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }
}
