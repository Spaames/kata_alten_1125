package com.example.backend_api.service;

import com.example.backend_api.model.Product;

import com.fasterxml.jackson.core.type.TypeReference; // for type handling
import com.fasterxml.jackson.databind.ObjectMapper; // java <-> json conversion
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {

    private final ObjectMapper mapper = new ObjectMapper();
    private final File file = new File("src/main/resources/products.json");

    public List<Product> getAll() {
        try {
            return mapper.readValue(file, new TypeReference<List<Product>>() {});
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public Product getById(int id) {
        return getAll().stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public Product save(Product product) {
        List<Product> products = getAll();

        int newId = products.stream().mapToInt(Product::getId).max().orElse(0) + 1;
        product.setId(newId);
        product.setCreatedAt(System.currentTimeMillis());
        product.setUpdatedAt(System.currentTimeMillis());

        products.add(product);
        writeJson(products);
        return product;
    }

    public Product update(int id, Product updatedProduct) {
        List<Product> products = getAll();
        for (int i = 0; i < products.size(); i++) {
            Product p = products.get(i);
            if (p.getId() == id) {
                updatedProduct.setId(p.getId());
                updatedProduct.setCreatedAt(p.getCreatedAt());
                updatedProduct.setUpdatedAt(System.currentTimeMillis());
                products.set(i, updatedProduct);
                writeJson(products);
                return updatedProduct;
            }
        }
        return null;
    }

    public boolean delete(int id) {
        List<Product> products = getAll();
        boolean removed = products.removeIf(p -> p.getId() == id);
        if (removed) {
            writeJson(products);
        }
        return removed;
    }

    private void writeJson(List<Product> products) {
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(file, products);
        } catch (Exception e) {
            throw new RuntimeException("Failed to write products to file: " + e.getMessage());
        }
    }

}
