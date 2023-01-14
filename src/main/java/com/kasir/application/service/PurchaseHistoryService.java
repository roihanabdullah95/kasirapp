package com.kasir.application.service;

import com.kasir.application.model.Product;
import com.kasir.application.model.PurchaseHistory;
import com.kasir.application.model.User;
import com.kasir.application.repository.ProductRepository;
import com.kasir.application.repository.PurchaseHistoryRepository;
import com.kasir.application.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class PurchaseHistoryService {
    @Autowired
    PurchaseHistoryRepository purchaseHistoryRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ProductRepository productRepository;

    public List<PurchaseHistory> getPurchaseHistory(Authentication authentication) {
        User user = userRepository.findByEmail(authentication.getName());
        return purchaseHistoryRepository.findByUser(user);
    }

    public List<PurchaseHistory> addPurchaseHistory(List<PurchaseHistory> purchaseHistory, Authentication authentication) {
        User user = userRepository.findByEmail(authentication.getName());
        List<Product> products = new ArrayList<>();
        List<User> userList = new ArrayList<>();
        for (PurchaseHistory p : purchaseHistory) {
            Product product = productRepository.findById(p.getProduct().getId()).orElse(null);
            p.setUser(user);
            p.setProduct(product);
            p.setCreatedAt(new Date());
            product.setStock(product.getStock() - p.getTotalProduct());
            product.setJumlahTerjual(product.getJumlahTerjual() + p.getTotalProduct());
            products.add(product);
        }
        user.setTotalPesanan(user.getTotalPesanan() + 1);
        userList.add(user);
        productRepository.saveAll(products);
        userRepository.saveAll(userList);
        return purchaseHistoryRepository.saveAll(purchaseHistory);
    }

}
