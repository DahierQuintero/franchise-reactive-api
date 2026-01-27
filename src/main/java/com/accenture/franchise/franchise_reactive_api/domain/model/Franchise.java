package com.accenture.franchise.franchise_reactive_api.domain.model;

import java.util.List;
import java.util.UUID;

public class Franchise {

    private final String id;
    private String name;
    private List<Branch> branches;

    public Franchise(String name, List<Branch> branches) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.branches = branches;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Branch> getBranches() {
        return branches;
    }

    public void updateName(String name) {
        this.name = name;
    }
}
