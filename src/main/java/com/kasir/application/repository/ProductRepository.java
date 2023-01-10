package com.kasir.application.repository;

import com.kasir.application.model.Product;
import com.kasir.application.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> getProductByUser(User user);
}
