package com.accenture.franchise.franchise_reactive_api.domain.model;

import java.util.List;
import java.util.UUID;

public class Branch {

    private final String id;
    private String name;
    private List<Product> products;

    public Branch(String name, List<Product> products) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.products = products;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void updateName(String name) {
        this.name = name;
    }
}
