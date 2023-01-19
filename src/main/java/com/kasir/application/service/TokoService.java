package com.kasir.application.service;

import com.kasir.application.model.Toko;
import com.kasir.application.model.User;
import com.kasir.application.repository.TokoRepository;
import com.kasir.application.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class TokoService {
    @Autowired
    TokoRepository tokoRepository;
    @Autowired
    UserRepository userRepository;

    public Toko getAllToko(Authentication authentication) {
        User user = userRepository.findByEmail(authentication.getName());
        return tokoRepository.getTokoByUser(user);
    }

    public Toko getTokoById(Long id) throws Exception {
        Toko toko = tokoRepository.findById(id).orElse(null);
        if (toko == null) throw new Exception("Toko not found!!!");
        return toko;
    }

    public Toko addToko(Toko toko, Authentication authentication) throws Exception {
        User user = userRepository.findByEmail(authentication.getName());
        toko.setUser(user);
        return tokoRepository.save(toko);
    }

    public Toko updateToko(Long id, Toko toko, Authentication authentication) throws Exception {
        Toko toko1 = tokoRepository.findById(id).orElse(null);
        User user = userRepository.findByEmail(authentication.getName());
        if (toko1 == null) {
            throw new Exception("Toko not found!!!");
        }
        if (!user.equals(toko1.getUser())) {
            throw new Exception("No permission to update this toko!!!");
        }
        toko1.setName(toko.getName());
        toko1.setPhoneNumber(toko.getPhoneNumber());
        toko1.setAddress(toko.getAddress());
        return tokoRepository.save(toko1);
    }

    public Toko deleteToko(Long id, Authentication authentication) throws Exception {
        Toko toko = tokoRepository.findById(id).orElse(null);
        User user = userRepository.findByEmail(authentication.getName());
        if (toko == null) {
            throw new Exception("Toko not found!!!");
        }
        if (!user.equals(toko.getUser())) {
            throw new Exception("No permission to delete this toko!!!");
        }
        tokoRepository.delete(toko);
        return toko;
    }
}
