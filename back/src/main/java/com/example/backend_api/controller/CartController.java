package com.example.backend_api.controller;

import com.example.backend_api.exception.ProductNotFoundException;
import com.example.backend_api.model.Cart;
import com.example.backend_api.service.CartService;
import com.example.backend_api.service.ProductService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;
    private final ProductService productService;

    public CartController(CartService cartService, ProductService productService) {
        this.cartService = cartService;
        this.productService = productService;
    }

    // GET /cart
    @GetMapping
    public ResponseEntity<Cart> getCart() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Cart cart = cartService.getByUserEmail(email);
        
        if (cart == null) {
            cart = new Cart();
            cart.setUserEmail(email);
        }
        
        return ResponseEntity.ok(cart);
    }

    // POST /cart/{productId}
    @PostMapping("/{productId}")
    public ResponseEntity<Cart> addProduct(@PathVariable int productId) {
        // Check if product exists
        if (productService.getById(productId) == null) {
            throw new ProductNotFoundException(productId);
        }
        
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Cart cart = cartService.addProduct(email, productId);
        return ResponseEntity.ok(cart);
    }

    // DELETE /cart/{productId}
    @DeleteMapping("/{productId}")
    public ResponseEntity<Cart> removeProduct(@PathVariable int productId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Cart cart = cartService.removeProduct(email, productId);
        return ResponseEntity.ok(cart);
    }

    // DELETE /cart
    @DeleteMapping
    public ResponseEntity<Map<String, String>> clearCart() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        cartService.clearCart(email);
        return ResponseEntity.ok(Map.of("message", "Cart cleared"));
    }
}