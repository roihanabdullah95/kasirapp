package com.kasir.application.controller;

import com.kasir.application.model.Toko;
import com.kasir.application.service.TokoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://127.0.0.1:5173")
@RequestMapping("/api/toko")
public class TokoController {
    @Autowired
    TokoService tokoService;

    @GetMapping(path = "/all")
    public ResponseEntity<?> getAllTokoByUser(Authentication authentication) {
        List<Toko> toko = tokoService.getAllToko(authentication);
        return new ResponseEntity<>(toko, HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<?> getTokoById(@PathVariable("id") Long id) throws Exception {
        Toko toko = tokoService.getTokoById(id);
        return new ResponseEntity<>(toko, HttpStatus.OK);
    }

    @PostMapping(path = "/add", consumes = "application/json")
    public ResponseEntity<?> addToko(@RequestBody Toko toko, Authentication authentication) throws Exception {
        Toko toko1 = tokoService.addToko(toko, authentication);
        return new ResponseEntity<>(toko1, HttpStatus.CREATED);
    }

    @PutMapping(path = "/{id}", consumes = "application/json")
    public ResponseEntity<?> updateToko(@PathVariable("id") Long id, @RequestBody Toko toko, Authentication authentication) throws Exception {
        Toko toko1 = tokoService.updateToko(id, toko, authentication);
        return new ResponseEntity<>(toko1, HttpStatus.OK);
    }

    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<?> deleteToko(@PathVariable("id") Long id, Authentication authentication) throws Exception {
        Toko toko = tokoService.deleteToko(id, authentication);
        return new ResponseEntity<>(toko, HttpStatus.OK);
    }
}