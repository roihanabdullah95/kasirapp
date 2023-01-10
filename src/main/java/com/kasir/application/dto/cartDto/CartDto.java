package com.kasir.application.dto.cartDto;

import java.util.List;

public class CartDto {
    private List<CartItemDto> cartItem;
    private Double totalPrice;
    private int quantity;

    public List<CartItemDto> getCartItem() {
        return cartItem;
    }

    public void setCartItem(List<CartItemDto> cartItem) {
        this.cartItem = cartItem;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
