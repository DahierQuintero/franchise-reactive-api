package com.accenture.franchise.franchise_reactive_api.application.service;

import com.accenture.franchise.franchise_reactive_api.api.dto.TopStockProductResponse;
import com.accenture.franchise.franchise_reactive_api.domain.model.Product;
import com.accenture.franchise.franchise_reactive_api.domain.repository.IFranchiseRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor
public class GetTopStockProductsByFranchiseService {

    private final IFranchiseRepository repository;

    public Mono<List<TopStockProductResponse>> getTopStockByFranchise(String franchiseId) {
        return repository.findById(franchiseId)
                .map(franchise ->
                        franchise.getBranches().stream()
                                .flatMap(branch ->
                                        branch.getProducts().stream()
                                                .max(Comparator.comparingInt(Product::getStock))
                                                .map(product ->
                                                        new TopStockProductResponse(
                                                                branch.getName(),
                                                                product.getName(),
                                                                product.getStock()
                                                        )
                                                )
                                                .stream()
                                )
                                .toList()
                );
    }
}
