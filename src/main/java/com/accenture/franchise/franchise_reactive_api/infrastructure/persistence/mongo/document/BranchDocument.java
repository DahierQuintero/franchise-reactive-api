package com.accenture.franchise.franchise_reactive_api.infrastructure.persistence.mongo.document;

import lombok.Data;

import java.util.List;

@Data
public class BranchDocument {

    private String id;
    private String name;
    private List<ProductDocument> products;
}