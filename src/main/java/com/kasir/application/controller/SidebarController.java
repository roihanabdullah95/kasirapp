package com.kasir.application.controller;

import com.kasir.application.model.Sidebar;
import com.kasir.application.service.SidebarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://127.0.0.1:5173")
@RequestMapping("/api/sidebar")
public class SidebarController {
    @Autowired
    SidebarService sidebarService;

    @GetMapping(path = "/all")
    public ResponseEntity<?> getAllCategoryByUser() {
        List<Sidebar> side = sidebarService.getAllSidebar();
        return new ResponseEntity<>(side, HttpStatus.OK);
    }
}
