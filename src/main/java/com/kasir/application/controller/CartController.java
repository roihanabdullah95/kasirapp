package com.kasir.application.controller;

import com.kasir.application.dto.cartDto.AddCartDto;
import com.kasir.application.dto.cartDto.CartDto;
import com.kasir.application.model.Cart;
import com.kasir.application.model.Product;
import com.kasir.application.service.CartService;
import com.kasir.application.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://127.0.0.1:5173")
@RequestMapping("/api/cart")
public class CartController {
    @Autowired
    CartService cartService;
    @Autowired
    ProductService productService;

    @GetMapping("/list")
    public ResponseEntity<?> getAllCart(Authentication authentication) throws Exception {
        CartDto cartDto = cartService.getListCart(authentication);
        return new ResponseEntity<>(cartDto, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addCart(@RequestBody AddCartDto addCartDto, Authentication authentication) throws Exception {
        Product product = productService.getProductById(addCartDto.getProductId());
        Cart cart = cartService.addCart(addCartDto, product, authentication);
        return new ResponseEntity<>(cart, HttpStatus.CREATED);
    }

    @PutMapping(path = "/update/{id}", consumes = "application/json")
    public ResponseEntity<?> updateCart(@PathVariable("id") Long id, @RequestBody Cart cart, Authentication authentication) throws Exception {
        Cart carts = cartService.updateCart(id, cart, authentication);
        return new ResponseEntity<>(carts, HttpStatus.OK);
    }

    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<?> deleteCart(@PathVariable("id") Long id, Authentication authentication) throws Exception {
        Cart carts = cartService.deleteCartItem(id, authentication);
        return new ResponseEntity<>(carts, HttpStatus.OK);
    }

    @DeleteMapping(path = "/delete")
    public ResponseEntity<?> deleteCart(Authentication authentication) {
        List<Cart> carts = cartService.deleteAllCart(authentication);
        return new ResponseEntity<>(carts, HttpStatus.OK);
    }
}
