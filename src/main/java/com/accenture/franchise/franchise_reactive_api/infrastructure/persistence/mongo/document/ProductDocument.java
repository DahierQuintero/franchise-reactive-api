package com.accenture.franchise.franchise_reactive_api.infrastructure.persistence.mongo.document;

import lombok.Data;

@Data
public class ProductDocument {

    private String id;
    private String name;
    private int stock;
}