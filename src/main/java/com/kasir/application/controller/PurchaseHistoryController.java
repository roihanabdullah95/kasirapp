package com.kasir.application.controller;

import com.kasir.application.model.Product;
import com.kasir.application.model.PurchaseHistory;
import com.kasir.application.service.PurchaseHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://127.0.0.1:5173")
@RequestMapping("/api/history")
public class PurchaseHistoryController {
    @Autowired
    PurchaseHistoryService purchaseHistoryService;

    @GetMapping(path = "/list")
    public ResponseEntity<?> PurchaseHistoryList(Authentication authentication) {
        List<PurchaseHistory> purchaseHistories = purchaseHistoryService.getPurchaseHistory(authentication);
        return new ResponseEntity<>(purchaseHistories, HttpStatus.OK);
    }

    @GetMapping(path = "/time-added")
    public ResponseEntity<?> findAllOrderByCreatedAtDesc(Authentication authentication) {
        List<PurchaseHistory> purchaseHistories = purchaseHistoryService.findAllOrderByCreatedAtDesc(authentication);
        return new ResponseEntity<>(purchaseHistories, HttpStatus.OK);
    }

    @PostMapping(path = "/add")
    public ResponseEntity<?> addPurchaseHistory(@RequestBody List<PurchaseHistory> purchaseHistories, Authentication authentication) {
        List<PurchaseHistory> purchaseHistories1 = purchaseHistoryService.addPurchaseHistory(purchaseHistories, authentication);
        return new ResponseEntity<>(purchaseHistories1, HttpStatus.CREATED);
    }
}
