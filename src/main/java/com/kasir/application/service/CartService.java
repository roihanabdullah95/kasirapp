package com.kasir.application.service;

import com.kasir.application.dto.cartDto.AddCartDto;
import com.kasir.application.dto.cartDto.CartDto;
import com.kasir.application.dto.cartDto.CartItemDto;
import com.kasir.application.model.Cart;
import com.kasir.application.model.Product;
import com.kasir.application.model.User;
import com.kasir.application.repository.CartRepository;
import com.kasir.application.repository.ProductRepository;
import com.kasir.application.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CartService {
    @Autowired
    CartRepository cartRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    UserRepository userRepository;

    private static CartItemDto getDtoFromCart(Cart cart) {
        return new CartItemDto(cart);
    }

    public Cart addCart(AddCartDto addCartDto, Product product, Authentication authentication) {
        User user = userRepository.findByEmail(authentication.getName());
        Cart cart = new Cart(addCartDto.getQuantity(), product, user);
        return cartRepository.save(cart);
    }

    public CartDto getListCart(Authentication authentication) {
        User user = userRepository.findByEmail(authentication.getName());
        List<Cart> cartList = cartRepository.getCartByUser(user);
        List<CartItemDto> cartItems = new ArrayList<>();
        CartDto cartDto = new CartDto();
        for (Cart cart : cartList) {
            CartItemDto cartItemDto = getDtoFromCart(cart);
            cartItems.add(cartItemDto);
        }
        double totalPrice = 0;
        for (CartItemDto cartItemDto : cartItems) {
            totalPrice += (cartItemDto.getProduct().getPrice() * cartItemDto.getQuantity());
        }
        int quantity = 0;
        for (CartItemDto cartItemDto : cartItems) {
            quantity += cartItemDto.getQuantity();
        }
        cartDto.setQuantity(quantity);
        cartDto.setCartItem(cartItems);
        cartDto.setTotalPrice(totalPrice);
        return cartDto;
    }

    public Cart updateCart(Long id, Cart cart, Authentication authentication) throws Exception {
        Cart carts = cartRepository.findById(id).orElse(null);
        User user = userRepository.findByEmail(authentication.getName());
        if (carts == null) {
            throw new Exception("Cart id not found!!!");
        }
        if (!user.equals(carts.getUser())) {
            throw new Exception("No permission to update this cart!!!");
        }
        carts.setQuantity(cart.getQuantity());
        carts.setCreatedAt(new Date());
        return cartRepository.save(carts);
    }

    public Cart deleteCartItem(Long id, Authentication authentication) throws Exception {
        User user = userRepository.findByEmail(authentication.getName());
        Cart cart = cartRepository.findById(id).orElse(null);
        if (cart == null) {
            throw new Exception("Cart not found!!!");
        }
        if (!user.equals(cart.getUser())) {
            throw new Exception("No permission to delete this post!!!");
        }
        cartRepository.delete(cart);
        return cart;
    }

    @Transactional
    public List<Cart> deleteAllCart(Authentication authentication) {
        User user = userRepository.findByEmail(authentication.getName());
        return cartRepository.deleteByUser(user);
    }
}
