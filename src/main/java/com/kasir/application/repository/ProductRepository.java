package com.kasir.application.repository;

import com.kasir.application.model.Product;
import com.kasir.application.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> getProductByUser(User user);

    @Query("SELECT p FROM Product p WHERE " +
            "p.name LIKE CONCAT('%',:name, '%')")
    List<Product> findByName(String name);

    @Query("FROM Product ORDER BY jumlah_terjual DESC")
    List<Product> findAllOrderByJumlahTerjualDesc();
    @Query("FROM Product ORDER BY created_at DESC")
    List<Product> findAllOrderByCreatedAtDesc();

}
