package com.example.car_rental_system_final.models;

public class UserInfo {
    private String id;
    private String email;
    private String name;

    public UserInfo(String id, String email, String name) {
        this.id = id;
        this.email = email;
        this.name = name;
    }

    // Getters
    public String getId() { return id; }
    public String getEmail() { return email; }
    public String getName() { return name; }
} 