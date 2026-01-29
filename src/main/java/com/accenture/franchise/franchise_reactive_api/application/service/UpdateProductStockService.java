package com.accenture.franchise.franchise_reactive_api.application.service;

import com.accenture.franchise.franchise_reactive_api.domain.repository.IFranchiseRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class UpdateProductStockService {

    private final IFranchiseRepository repository;

    public Mono<Void> updateStock(
            String franchiseId,
            String branchName,
            String productName,
            int newStock
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

                    var productOpt = branchOpt.get().getProducts().stream()
                            .filter(p -> p.getName().equalsIgnoreCase(productName))
                            .findFirst();

                    if (productOpt.isEmpty()) {
                        return Mono.empty();
                    }

                    productOpt.get().setStock(newStock);
                    return repository.save(franchise);
                })
                .then();
    }

    private String normalize(String branchName) {
        return branchName.replace("-", " ").trim();
    }
}
