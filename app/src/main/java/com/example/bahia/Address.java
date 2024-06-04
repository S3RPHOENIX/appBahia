package com.example.bahia;

public class Address {
    private String id;
    private String city;
    private String postalCode;
    private String address;
    private String state;
    private String name;
    private String phone;

    public Address() {
    }

    public Address(String id, String city, String postalCode, String address, String state, String name, String phone) {
        this.id = id;
        this.city = city;
        this.postalCode = postalCode;
        this.address = address;
        this.state = state;
        this.name = name;
        this.phone = phone;
    }

    public String getId() {
        return id;
    }

    public String getCity() {
        return city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getAddress() {
        return address;
    }

    public String getState() {
        return state;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }
}
