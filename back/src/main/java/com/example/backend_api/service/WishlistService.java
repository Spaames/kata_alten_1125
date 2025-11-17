package com.example.backend_api.service;

import com.example.backend_api.model.Wishlist;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class WishlistService {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final File file = new File("src/main/resources/wishlists.json");

    public List<Wishlist> getAll() {
        try {
            return objectMapper.readValue(file, new TypeReference<List<Wishlist>>() {});
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    public Wishlist getByUserEmail(String email) {
        return getAll().stream()
                .filter(w -> w.getUserEmail().equals(email))
                .findFirst()
                .orElse(null);
    }

    public Wishlist addProduct(String email, int productId) {
        List<Wishlist> wishlists = getAll();
        Wishlist userWishlist = getByUserEmail(email);

        if (userWishlist == null) {
            userWishlist = new Wishlist();
            userWishlist.setUserEmail(email);
            userWishlist.setProductIds(new ArrayList<>());
            wishlists.add(userWishlist);
        }

        if (!userWishlist.getProductIds().contains(productId)) {
            userWishlist.getProductIds().add(productId);
        }

        writeJson(wishlists);
        return userWishlist;
    }

    public Wishlist removeProduct(String email, int productId) {
        List<Wishlist> wishlists = getAll();
        Wishlist userWishlist = getByUserEmail(email);

        if (userWishlist != null) {
            userWishlist.getProductIds().remove(Integer.valueOf(productId));
            writeJson(wishlists);
        }

        return userWishlist;
    }

    public void clearWishlist(String email) {
        List<Wishlist> wishlists = getAll();
        wishlists.removeIf(w -> w.getUserEmail().equals(email));
        writeJson(wishlists);
    }

    private void writeJson(List<Wishlist> wishlists) {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, wishlists);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}