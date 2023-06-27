package com.kasir.application.service;

import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.kasir.application.dto.AddProductDto;
import com.kasir.application.dto.TokoDto;
import com.kasir.application.model.Category;
import com.kasir.application.model.Product;
import com.kasir.application.model.Toko;
import com.kasir.application.model.User;
import com.kasir.application.repository.TokoRepository;
import com.kasir.application.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

@Service
public class TokoService {
    @Autowired
    TokoRepository tokoRepository;
    @Autowired
    UserRepository userRepository;

    private static final String DOWNLOAD_URL = "https://firebasestorage.googleapis.com/v0/b/kasir-application-76401.appspot.com/o/%s?alt=media";

    public Toko getAllToko(Authentication authentication) {
        User user = userRepository.findByEmail(authentication.getName());
        return tokoRepository.getTokoByUser(user);
    }

    public Toko getTokoById(Long id) throws Exception {
        Toko toko = tokoRepository.findById(id).orElse(null);
        if (toko == null) throw new Exception("Toko not found!!!");
        return toko;
    }

    public Toko addToko(TokoDto tokodto, MultipartFile multipartFile, Authentication authentication) throws Exception {
        User user = userRepository.findByEmail(authentication.getName());
        String image = imageConverter(multipartFile);
        Toko toko = new Toko(image, tokodto.getName(), tokodto.getAddress(), tokodto.getPhoneNumber(), user);
        toko.setUser(user);
        return tokoRepository.save(toko);
    }

    public Toko updateToko(Long id, TokoDto tokodto, MultipartFile multipartFile, Authentication authentication) throws Exception {
        Toko toko1 = tokoRepository.findById(id).orElse(null);
        User user = userRepository.findByEmail(authentication.getName());
        String image = imageConverter(multipartFile);
        if (toko1 == null) {
            throw new Exception("Toko not found!!!");
        }
        if (!user.equals(toko1.getUser())) {
            throw new Exception("No permission to update this toko!!!");
        }
        toko1.setName(tokodto.getName());
        toko1.setPhoneNumber(tokodto.getPhoneNumber());
        toko1.setAddress(tokodto.getAddress());
        toko1.setImage(image);
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
    private String getExtension(String fileName) {
        return  fileName.split("\\.")[0];
    }

    private File convertFile(MultipartFile multipartFile, String fileName) throws IOException {
        File file = new File(fileName);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(multipartFile.getBytes());
            fos.close();
        }
        return file;
    }

    private String imageConverter(MultipartFile multipartFile) throws Exception {
        try {
            String fileName = getExtension(multipartFile.getOriginalFilename());
            File file = convertFile(multipartFile, fileName);
            var RESPONSE_URL = uploadFile(file, fileName);
            file.delete();
            return RESPONSE_URL;
        } catch (Exception e) {
            e.getStackTrace();
            throw new Exception("Error upload file!");
        }
    }


    private String uploadFile(File file, String fileName) throws IOException {
        BlobId blobId = BlobId.of("kasir-application-76401.appspot.com", fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("media").build();
        Credentials credentials = GoogleCredentials.fromStream(new FileInputStream("./src/main/resources/serviceAccountKey.json"));
        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        storage.create(blobInfo, Files.readAllBytes(file.toPath()));
        return String.format(DOWNLOAD_URL, URLEncoder.encode(fileName, StandardCharsets.UTF_8));
    }
}
