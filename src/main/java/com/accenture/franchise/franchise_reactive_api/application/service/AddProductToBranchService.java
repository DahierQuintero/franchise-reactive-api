package com.accenture.franchise.franchise_reactive_api.application.service;

import com.accenture.franchise.franchise_reactive_api.domain.model.Product;
import com.accenture.franchise.franchise_reactive_api.domain.repository.IFranchiseRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class AddProductToBranchService {

    private final IFranchiseRepository repository;

    public Mono<Void> addProduct(
            String franchiseId,
            String branchName,
            String productName,
            int stock
    ) {
        String normalizedBranchName = normalize(branchName);

        return repository.findById(franchiseId)
                .flatMap(franchise -> {
                    var branchOpt = franchise.getBranches().stream()
                            .filter(b -> b.getName().equalsIgnoreCase(normalizedBranchName))
                            .findFirst();

                    if (branchOpt.isEmpty()) {
                        return Mono.empty();
                    }

                    var branch = branchOpt.get();
                    branch.getProducts().add(
                            new Product(productName, stock)
                    );

                    return repository.save(franchise);
                })
                .then();
    }

    private String normalize(String branchName) {
        return branchName
                .replace("-", " ")
                .trim();
    }
}
