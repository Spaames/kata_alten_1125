package com.example.backend_api.controller;

import com.example.backend_api.exception.ForbiddenException;
import com.example.backend_api.exception.ProductNotFoundException;
import com.example.backend_api.model.Product;
import com.example.backend_api.service.ProductService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService service;
    private static final String ADMIN_EMAIL = "admin@admin.com"; //hardcoded for the example

    public ProductController(ProductService service) {
        this.service = service;
    }

    private void checkAdmin() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!ADMIN_EMAIL.equals(email)) {
            throw new ForbiddenException("Admin access required");
        }
    }

    // GET /products
    @GetMapping
    public List<Product> getAll() {
        return service.getAll();
    }

    // GET /products/{id}
    @GetMapping("/{id}")
    public Product getById(@PathVariable int id) {
        Product product = service.getById(id);
        if (product == null) {
            throw new ProductNotFoundException(id);
        }
        return product;
    }

    // POST /products
    @PostMapping
    public ResponseEntity<Product> create(@RequestBody Product product) {
        checkAdmin();
        Product saved = service.save(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // PUT /products/{id}
    @PutMapping("/{id}")
    public Product update(@PathVariable int id, @RequestBody Product product) {
        checkAdmin();
        Product updated = service.update(id, product);
        if (updated == null) {
            throw new ProductNotFoundException(id);
        }
        return updated;
    }

    // DELETE /products/{id}
    @DeleteMapping("/{id}")
    public Map<String, String> delete(@PathVariable int id) {
        checkAdmin();
        boolean deleted = service.delete(id);
        if (!deleted) {
            throw new ProductNotFoundException(id);
        }
        Map<String, String> response = new HashMap<>();
        response.put("message", "Product with id " + id + " deleted successfully");
        return response;
    }
}