package com.example.bahia;

import java.util.List;

public class Purchase {
    private String addressId;
    private List<Seafood> items;
    private String paymentMethod;
    private long timestamp;

    // Constructor sin argumentos
    public Purchase() {
    }

    // Getters y setters
    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public List<Seafood> getItems() {
        return items;
    }

    public void setItems(List<Seafood> items) {
        this.items = items;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}