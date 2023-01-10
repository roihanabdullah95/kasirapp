package com.kasir.application.controller;

import com.kasir.application.dto.AddProductDto;
import com.kasir.application.model.Category;
import com.kasir.application.model.Product;
import com.kasir.application.service.CategoryService;
import com.kasir.application.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://127.0.0.1:5173")
@RequestMapping("/api/product")
public class ProductController {
    @Autowired
    ProductService productService;
    @Autowired
    CategoryService categoryService;

    @GetMapping(path = "/all")
    public ResponseEntity<?> getAllProductByUser(Authentication authentication) {
        List<Product> products = productService.getAllProduct(authentication);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<?> getProductById(@PathVariable("id") Long id) throws Exception {
        Product products = productService.getProductById(id);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @PostMapping(path = "/add", consumes = "multipart/form-data")
    public ResponseEntity<?> addProduct(@RequestPart("file") MultipartFile multipartFile, AddProductDto addProductDto, Authentication authentication) throws Exception {
        Category category = categoryService.getCategoryById(addProductDto.getCategoryId());
        Product products = productService.addProduct(addProductDto, multipartFile, authentication, category);
        return new ResponseEntity<>(products, HttpStatus.CREATED);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable("id") Long id, @RequestBody Product product, Authentication authentication) throws Exception {
        Product products = productService.updateProduct(id, product, authentication);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable("id") Long id, Authentication authentication) throws Exception {
        Product product = productService.deleteProduct(id, authentication);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

}
