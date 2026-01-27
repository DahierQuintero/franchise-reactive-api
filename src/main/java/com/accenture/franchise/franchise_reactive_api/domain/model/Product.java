package com.accenture.franchise.franchise_reactive_api.domain.model;

import java.util.UUID;

public class Product {

    private final String id;
    private String name;
    private int stock;

    public Product(String name, int stock) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.stock = stock;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getStock() {
        return stock;
    }

    public void updateStock(int stock) {
        this.stock = stock;
    }

    public void updateName(String name) {
        this.name = name;
    }
}
