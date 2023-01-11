package com.kasir.application.service;

import com.kasir.application.model.Category;
import com.kasir.application.model.Sidebar;
import com.kasir.application.model.User;
import com.kasir.application.repository.SidebarRepository;
import com.kasir.application.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SidebarService {
    @Autowired
    SidebarRepository sidebarRepository;

    public List<Sidebar> getAllSidebar() {
        return sidebarRepository.findAll();
    }
}
