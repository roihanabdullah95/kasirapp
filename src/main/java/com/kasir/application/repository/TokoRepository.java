package com.kasir.application.repository;

import com.kasir.application.model.Toko;
import com.kasir.application.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TokoRepository extends JpaRepository<Toko, Long> {
    List<Toko> getTokoByUser(User user);
}
