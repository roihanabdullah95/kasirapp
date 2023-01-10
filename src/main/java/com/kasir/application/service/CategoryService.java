package com.kasir.application.service;

import com.kasir.application.model.Category;
import com.kasir.application.model.User;
import com.kasir.application.repository.CategoryRepository;
import com.kasir.application.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    UserRepository userRepository;

    public List<Category> getAllCategory(Authentication authentication) {
        User user = userRepository.findByEmail(authentication.getName());
        return categoryRepository.getCategoryByUser(user);
    }

    public Category getCategoryById(Long id) throws Exception {
        Category categories = categoryRepository.findById(id).orElse(null);
        if (categories == null) throw new Exception("Category not found!!!");
        return categories;
    }

    public Category addCategory(Category category, Authentication authentication) throws Exception {
        User user = userRepository.findByEmail(authentication.getName());
        category.setUser(user);
        return categoryRepository.save(category);
    }

    public Category updateCategory(Long id, Category category, Authentication authentication) throws Exception {
        Category categories = categoryRepository.findById(id).orElse(null);
        User user = userRepository.findByEmail(authentication.getName());
        if (categories == null) {
            throw new Exception("Category not found!!!");
        }
        if (!user.equals(categories.getUser())) {
            throw new Exception("No permission to update this category!!!");
        }
        categories.setName(category.getName());
        return categoryRepository.save(categories);
    }

    public Category deleteCategory(Long id, Authentication authentication) throws Exception {
        Category categories = categoryRepository.findById(id).orElse(null);
        User user = userRepository.findByEmail(authentication.getName());
        if (categories == null) {
            throw new Exception("Category not found!!!");
        }
        if (!user.equals(categories.getUser())) {
            throw new Exception("No permission to delete this category!!!");
        }
        return categories;
    }
}
