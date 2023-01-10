package com.kasir.application.repository;

import com.kasir.application.model.Cart;
import com.kasir.application.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Long> {
    List<Cart> getCartByUser(User user);
    List<Cart> deleteByUser(User user);
}
