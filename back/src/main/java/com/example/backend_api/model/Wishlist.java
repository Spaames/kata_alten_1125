package com.example.backend_api.model;

import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class Wishlist {
    private String userEmail; // to identify the owner of the wishlist
    private List<Integer> productIds = new ArrayList<>();
}