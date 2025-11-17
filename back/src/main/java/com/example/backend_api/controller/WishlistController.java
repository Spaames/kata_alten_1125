package com.example.backend_api.controller;

import com.example.backend_api.exception.ProductNotFoundException;
import com.example.backend_api.model.Wishlist;
import com.example.backend_api.service.ProductService;
import com.example.backend_api.service.WishlistService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/wishlist")
public class WishlistController {

    private final WishlistService wishlistService;
    private final ProductService productService;

    public WishlistController(WishlistService wishlistService, ProductService productService) {
        this.wishlistService = wishlistService;
        this.productService = productService;
    }

    // GET /wishlist
    @GetMapping
    public ResponseEntity<Wishlist> getWishlist() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Wishlist wishlist = wishlistService.getByUserEmail(email);
        
        if (wishlist == null) {
            wishlist = new Wishlist();
            wishlist.setUserEmail(email);
        }
        
        return ResponseEntity.ok(wishlist);
    }

    // POST /wishlist/{productId}
    @PostMapping("/{productId}")
    public ResponseEntity<Wishlist> addProduct(@PathVariable int productId) {
        // Check if product exists
        if (productService.getById(productId) == null) {
            throw new ProductNotFoundException(productId);
        }
        
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Wishlist wishlist = wishlistService.addProduct(email, productId);
        return ResponseEntity.ok(wishlist);
    }

    // DELETE /wishlist/{productId}
    @DeleteMapping("/{productId}")
    public ResponseEntity<Wishlist> removeProduct(@PathVariable int productId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Wishlist wishlist = wishlistService.removeProduct(email, productId);
        return ResponseEntity.ok(wishlist);
    }

    // DELETE /wishlist
    @DeleteMapping
    public ResponseEntity<Map<String, String>> clearWishlist() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        wishlistService.clearWishlist(email);
        return ResponseEntity.ok(Map.of("message", "Wishlist cleared"));
    }
}