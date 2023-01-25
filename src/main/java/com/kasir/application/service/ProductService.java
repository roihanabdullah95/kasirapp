package com.kasir.application.service;

import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.kasir.application.dto.AddProductDto;
import com.kasir.application.model.Category;
import com.kasir.application.model.Product;
import com.kasir.application.model.User;
import com.kasir.application.repository.ProductRepository;
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
import java.time.LocalDate;
import java.time.Year;
import java.util.Date;
import java.util.List;

@Service
public class ProductService {

    private static final String DOWNLOAD_URL = "https://firebasestorage.googleapis.com/v0/b/kasir-application-76401.appspot.com/o/%s?alt=media";

    @Autowired
    ProductRepository productRepository;

    @Autowired
    UserRepository userRepository;

    public List<Product> getAllProduct(Authentication authentication) {
        User user = userRepository.findByEmail(authentication.getName());
        return productRepository.getProductByUser(user);
    }

    public List<Product> findAllOrderByJumlahTejualDesc() {
        return productRepository.findAllOrderByJumlahTerjualDesc();
    }

    public List<Product> findAllOrderByCreatedAtDesc() {
        return productRepository.findAllOrderByCreatedAtDesc();
    }

    public List<Product> findProductByName(String name) {
        List<Product> products = productRepository.findByName(name);
        return products;
    }

    public Product getProductById(Long id) throws Exception{
        Product product = productRepository.findById(id).orElse(null);
        if (product == null) throw new Exception("Product not found!!!");
        return product;
    }

    public Product addProduct(AddProductDto addProductDto, MultipartFile multipartFile, Authentication authentication, Category category) throws Exception {
        Date date = new Date();
        String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        User user = userRepository.findByEmail(authentication.getName());
        String image = imageConverter(multipartFile);
        Product products = new Product(image, addProductDto.getName(), addProductDto.getPrice(), addProductDto.getStock(), addProductDto.getDescription(), category);
        products.setCreatedDay(LocalDate.now().getDayOfMonth());
        products.setCreatedMonth(months[date.getMonth()]);
        products.setCreatedYear(Year.now().getValue());
        products.setUser(user);
        return productRepository.save(products);
    }

    public Product updateProduct(Long id, AddProductDto addProductDto, Authentication authentication) throws Exception {
        User user = userRepository.findByEmail(authentication.getName());
        Product products = productRepository.findById(id).orElse(null);
        if (!user.equals(products.getUser())) {
            throw new Exception("No permission to update this product!!!");
        }
        products.setName(addProductDto.getName());
        products.setDescription(addProductDto.getDescription());
        products.setPrice(addProductDto.getPrice());
        products.setStock(addProductDto.getStock());
        return productRepository.save(products);
    }

    public Product deleteProduct(Long id, Authentication authentication) throws Exception {
        Product product = productRepository.findById(id).orElse(null);
        User user = userRepository.findByEmail(authentication.getName());
        if (product == null) {
            throw new Exception("Product not found!!!");
        }
        if (!user.equals(product.getUser())) {
            throw new Exception("No permission to delete this product!!!");
        }
        productRepository.delete(product);
        return product;
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

    private String uploadFile(File file, String fileName) throws IOException {
        BlobId blobId = BlobId.of("kasir-application-76401.appspot.com", fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("media").build();
        Credentials credentials = GoogleCredentials.fromStream(new FileInputStream("./src/main/resources/serviceAccountKey.json"));
        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        storage.create(blobInfo, Files.readAllBytes(file.toPath()));
        return String.format(DOWNLOAD_URL, URLEncoder.encode(fileName, StandardCharsets.UTF_8));
    }

}
