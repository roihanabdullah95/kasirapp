package com.kasir.application.repository;

import com.kasir.application.model.Toko;
import com.kasir.application.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokoRepository extends JpaRepository<Toko, Long> {
    Toko getTokoByUser(User user);
}
