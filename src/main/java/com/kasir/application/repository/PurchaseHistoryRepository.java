package com.kasir.application.repository;

import com.kasir.application.model.PurchaseHistory;
import com.kasir.application.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PurchaseHistoryRepository extends JpaRepository<PurchaseHistory, Long> {
    List<PurchaseHistory> findByUser(User user);
    @Query("FROM PurchaseHistory ORDER BY created_at DESC")
    List<PurchaseHistory> findAllOrderByCreatedAtDesc();
}
