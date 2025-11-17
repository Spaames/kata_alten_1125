package com.example.backend_api.model;

import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class Cart {
    private String userEmail; // to identify the owner of the cart
    private List<Integer> productIds = new ArrayList<>();
}