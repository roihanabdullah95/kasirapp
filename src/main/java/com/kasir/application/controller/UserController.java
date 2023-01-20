package com.kasir.application.controller;

import com.kasir.application.model.User;
import com.kasir.application.payload.request.JwtRequest;
import com.kasir.application.payload.response.CommonResponse;
import com.kasir.application.payload.response.JwtResponse;
import com.kasir.application.payload.response.ResponseHelper;
import com.kasir.application.repository.UserRepository;
import com.kasir.application.service.UserService;
import com.kasir.application.utill.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin(origins = "http://127.0.0.1:5173")
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserId(@PathVariable("id") Long id) {
        User user = userService.getUserId(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping(path = "/register", consumes = "application/json")
    public CommonResponse<String> register(@RequestBody User user) {
        return ResponseHelper.ok(userService.createUser(user));
    }

    @PostMapping(path = "/login", consumes = "application/json", produces = "application/json")
    public CommonResponse<Map<String, Object>> login(@RequestBody JwtRequest jwtRequest) {
        return ResponseHelper.ok(userService.login(jwtRequest));
    }

}
