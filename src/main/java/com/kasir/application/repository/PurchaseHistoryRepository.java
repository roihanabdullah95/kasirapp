package com.kasir.application.repository;

import com.kasir.application.model.PurchaseHistory;
import com.kasir.application.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PurchaseHistoryRepository extends JpaRepository<PurchaseHistory, Long> {
    List<PurchaseHistory> findByUser(User user);
}
