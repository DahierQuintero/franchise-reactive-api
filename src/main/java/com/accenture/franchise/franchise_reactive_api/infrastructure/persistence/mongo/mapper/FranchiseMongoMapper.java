package com.accenture.franchise.franchise_reactive_api.infrastructure.persistence.mongo.mapper;

import com.accenture.franchise.franchise_reactive_api.domain.model.Branch;
import com.accenture.franchise.franchise_reactive_api.domain.model.Franchise;
import com.accenture.franchise.franchise_reactive_api.domain.model.Product;
import com.accenture.franchise.franchise_reactive_api.infrastructure.persistence.mongo.document.BranchDocument;
import com.accenture.franchise.franchise_reactive_api.infrastructure.persistence.mongo.document.FranchiseDocument;
import com.accenture.franchise.franchise_reactive_api.infrastructure.persistence.mongo.document.ProductDocument;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class FranchiseMongoMapper {

    public static FranchiseDocument toDocument(Franchise franchise) {
        FranchiseDocument doc = new FranchiseDocument();
        doc.setId(franchise.getId());
        doc.setName(franchise.getName());
        doc.setBranches(
                franchise.getBranches().stream().map(branch -> {
                    BranchDocument bd = new BranchDocument();
                    bd.setId(branch.getId());
                    bd.setName(branch.getName());
                    bd.setProducts(
                            branch.getProducts().stream().map(product -> {
                                ProductDocument pd = new ProductDocument();
                                pd.setName(product.getName());
                                pd.setStock(product.getStock());
                                return pd;
                            }).collect(Collectors.toCollection(ArrayList::new))
                    );
                    return bd;
                }).collect(Collectors.toCollection(ArrayList::new))
        );
        return doc;
    }

    public static Franchise toDomain(FranchiseDocument doc) {
        return new Franchise(
                doc.getId(),
                doc.getName(),
                doc.getBranches().stream().map(b ->
                    new Branch(
                            b.getId(),
                            b.getName(),
                            b.getProducts().stream().map(p ->
                                    new Product(
                                            p.getName(),
                                            p.getStock()
                                    )
                            ).collect(Collectors.toCollection(ArrayList::new))
                    )
                ).collect(Collectors.toCollection(ArrayList::new))
        );
    }
}
