package com.accenture.franchise.franchise_reactive_api.infrastructure.persistence.mongo.document;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "franchises")
public class FranchiseDocument {

    @Id
    private String id;
    private String name;
    private List<BranchDocument> branches;
}