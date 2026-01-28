package com.accenture.franchise.franchise_reactive_api.application.service;

import com.accenture.franchise.franchise_reactive_api.domain.model.Branch;
import com.accenture.franchise.franchise_reactive_api.domain.repository.IFranchiseRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.ArrayList;

@RequiredArgsConstructor
public class AddBranchToFranchiseService {

    private final IFranchiseRepository repository;

    public Mono<Void> addBranch(String franchiseId, String branchName) {
        return repository.findById(franchiseId)
                .flatMap(franchise -> {
                    var branches = new ArrayList<>(franchise.getBranches());
                    branches.add(new Branch(branchName, new ArrayList<>()));
                    franchise.getBranches().clear();
                    franchise.getBranches().addAll(branches);
                    return repository.save(franchise);
                })
                .then();
    }
}
