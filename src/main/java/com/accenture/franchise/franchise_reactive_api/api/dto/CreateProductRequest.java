package com.accenture.franchise.franchise_reactive_api.api.dto;

public record CreateProductRequest(
        String name,
        int stock
) { }
