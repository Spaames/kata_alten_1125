package com.example.backend_api.model;

import lombok.Data; // lombok annotation to generate getters, setters, and other utility methods

@Data
public class Product {
    private int id;
    private String code;
    private String name;
    private String description;
    private String image;
    private String category;
    private double price;
    private int quantity;
    private String internalReference;
    private int shellId;
    private String inventoryStatus;
    private int rating;
    private long createdAt;
    private long updatedAt;
}
