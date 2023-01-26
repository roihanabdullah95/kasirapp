package com.kasir.application.repository;

import com.kasir.application.model.Product;
import com.kasir.application.model.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> getProductByUser(User user);

    @Query("SELECT p FROM Product p WHERE " +
            "p.name LIKE CONCAT('%',:name, '%') and p.user LIKE CONCAT('%',:user, '%')")
    List<Product> findByName(User user, String name);

    List<Product> findProductPopularByUser(User user, Sort jumlahTerjual);
    List<Product> findProductTimeAddedByUser(User user, Sort createdAt);
}
