package com.kasir.application.service;

import com.kasir.application.exception.InternalErrorException;
import com.kasir.application.model.User;
import com.kasir.application.payload.request.JwtRequest;
import com.kasir.application.repository.UserRepository;
import com.kasir.application.utill.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    private void authenticate(String email, String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        } catch (BadCredentialsException e) {
            if (email.isEmpty() || password.isEmpty())
                throw new InternalErrorException("Email & password must be filled!");
            if (email.isEmpty()) throw new InternalErrorException("Email must be filled!");
            if (password.isEmpty()) throw new InternalErrorException("Password must be filled!");

            throw new InternalErrorException("Email or password not found!");
        }
    }

    public Map<String, Object> login(JwtRequest jwtRequest) {
        authenticate(jwtRequest.getEmail(), jwtRequest.getPassword());

        final UserDetails userDetails = loadUserByUsername(jwtRequest.getEmail());
        final String token = jwtTokenUtil.generateToken(userDetails);
        User user = userRepository.findByEmail(jwtRequest.getEmail());

        Map<String, Object> response = new HashMap<>();
        response.put("userId", user.getId());
        response.put("token", token);
        return response;
    }

    public String createUser(User user) {
        String UserUsername = user.getUsername().trim();
        String UserEmail = user.getEmail().trim();
        String UserPassword = user.getPassword().trim();

        boolean UsernameIsNotvalid = (UserUsername == null) || !UserUsername.matches("[A-Za-z0-9_]+");
        boolean EmailIsNotValid = (UserEmail == null) || !UserEmail.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
        boolean PasswordIsNotValid = (UserPassword == null)
                || !UserPassword.matches("^(?=.*[0-9])(?=.*[a-z])(?=\\S+$).{8,20}");

        if (UsernameIsNotvalid) throw new InternalErrorException("Username not valid!");
        if (EmailIsNotValid) throw new InternalErrorException("Email not valid!");
        if (PasswordIsNotValid) throw new InternalErrorException("Password not valid!");
        if (userExists(user)) throw new InternalErrorException("User already exists!");

        user.setTotalPesanan(0L);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return "Register success!";
    }

    public User getUserId(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
                new ArrayList<>());
    }

    private Boolean userExists(User user) {
        if (userRepository.findByUsername(user.getUsername()) != null) return true;
        if (userRepository.findByEmail(user.getEmail()) != null) return true;
        return false;
    }

}
