package com.kasir.application.repository;

import com.kasir.application.model.Sidebar;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SidebarRepository extends JpaRepository<Sidebar, Long> {
}
