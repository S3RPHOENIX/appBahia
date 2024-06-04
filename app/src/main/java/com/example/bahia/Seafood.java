package com.example.bahia;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Seafood implements Serializable {
    private String id;
    private String name;
    private String description;
    private double price;
    private String imageUrl; // New field for image URL
    private int quantity;

    // Constructor sin argumentos
    public Seafood() {
    }

    public Seafood(String id, String name, String description, double price, String imageUrl) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
        this.quantity = 1; // Default quantity is 1
    }

    public Seafood(String id, String name, String description, double price, String imageUrl, int quantity) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
        this.quantity = quantity;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("name", name);
        map.put("description", description);
        map.put("price", price);
        map.put("imageUrl", imageUrl); // Store the image URL as a string
        map.put("quantity", quantity);
        return map;
    }
}