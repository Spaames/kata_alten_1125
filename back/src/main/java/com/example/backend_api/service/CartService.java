package com.example.backend_api.service;

import com.example.backend_api.model.Cart;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class CartService {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final File file = new File("src/main/resources/carts.json");

    public List<Cart> getAll() {
        try {
            return objectMapper.readValue(file, new TypeReference<List<Cart>>() {});
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    public Cart getByUserEmail(String email) {
        return getAll().stream()
                .filter(c -> c.getUserEmail().equals(email))
                .findFirst()
                .orElse(null);
    }

    public Cart addProduct(String email, int productId) {
        List<Cart> carts = getAll();
        Cart userCart = getByUserEmail(email);

        if (userCart == null) {
            // create a new cart for this user
            userCart = new Cart();
            userCart.setUserEmail(email);
            userCart.setProductIds(new ArrayList<>());
            carts.add(userCart);
        }

        // add the product if it's not already in the cart
        if (!userCart.getProductIds().contains(productId)) {
            userCart.getProductIds().add(productId);
        }

        writeJson(carts);
        return userCart;
    }

    public Cart removeProduct(String email, int productId) {
        List<Cart> carts = getAll();
        Cart userCart = getByUserEmail(email);

        if (userCart != null) {
            userCart.getProductIds().remove(Integer.valueOf(productId));
            writeJson(carts);
        }

        return userCart;
    }

    public void clearCart(String email) {
        List<Cart> carts = getAll();
        carts.removeIf(c -> c.getUserEmail().equals(email));
        writeJson(carts);
    }

    private void writeJson(List<Cart> carts) {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, carts);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}