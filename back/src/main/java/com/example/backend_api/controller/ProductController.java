package com.example.backend_api.controller;

import com.example.backend_api.exception.ProductNotFoundException;
import com.example.backend_api.model.Product;
import com.example.backend_api.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity; //wrapper
import org.springframework.web.bind.annotation.*; //endpoints

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/products") //base url
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
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
        Product saved = service.save(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // PUT /products/{id}
    @PutMapping("/{id}")
    public Product update(@PathVariable int id, @RequestBody Product product) {
        Product updated = service.update(id, product);
        if (updated == null) {
            throw new ProductNotFoundException(id);
        }
        return updated;
    }

    // DELETE /products/{id}
    @DeleteMapping("/{id}")
    public Map<String, String> delete(@PathVariable int id) {
        boolean deleted = service.delete(id);
        if (!deleted) {
            throw new ProductNotFoundException(id);
        }
        Map<String, String> response = new HashMap<>();
        response.put("message", "Product with id " + id + " deleted successfully");
        return response;
    }
}
