package com.kasir.application.repository;

import com.kasir.application.model.Category;
import com.kasir.application.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> getCategoryByUser(User user);
}
