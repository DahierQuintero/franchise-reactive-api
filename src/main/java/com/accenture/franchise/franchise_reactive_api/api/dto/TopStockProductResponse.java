package com.accenture.franchise.franchise_reactive_api.api.dto;

public record TopStockProductResponse(
        String branchName,
        String productName,
        int stock
) { }
