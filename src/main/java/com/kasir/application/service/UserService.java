package com.kasir.application.service;

import com.kasir.application.model.User;
import com.kasir.application.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

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

    public User createUser(User user) throws Exception {
        String UserUsername = user.getUsername().trim();
        String UserEmail = user.getEmail().trim();
        String UserPassword = user.getPassword().trim();

        boolean UsernameIsNotvalid = (UserUsername == null) || !UserUsername.matches("[A-Za-z0-9_]+");
        boolean EmailIsNotValid = (UserEmail == null) || !UserEmail.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
        boolean PasswordIsNotValid = (UserPassword == null)
                || !UserPassword.matches("^(?=.*[0-9])(?=.*[a-z])(?=\\S+$).{8,20}");

        if (UsernameIsNotvalid) throw new Exception("Username not valid!");
        if (EmailIsNotValid) throw new Exception("Email not valid!");
        if (PasswordIsNotValid) throw new Exception("Password not valid!");
        if (userExists(user)) throw new Exception("User already exists!");

        user.setTotalPesanan(0L);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

}
