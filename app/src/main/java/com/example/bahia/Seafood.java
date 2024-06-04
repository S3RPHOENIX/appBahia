package com.example.bahia;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Seafood implements Serializable {
    private String id;
    private String name;
    private String description;
    private double price;
    private int imageResId;
    private int quantity;

    // Constructor sin argumentos
    public Seafood() {
    }

    public Seafood(String id, String name, String description, double price, int imageResId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageResId = imageResId;
        this.quantity = 1; // Default quantity is 1
    }

    public Seafood(String id, String name, String description, double price, int imageResId, int quantity) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageResId = imageResId;
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

    public int getImageResId() {
        return imageResId;
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
        map.put("image", imageResId); // Store the image resource ID as an integer
        map.put("quantity", quantity);
        return map;
    }
}