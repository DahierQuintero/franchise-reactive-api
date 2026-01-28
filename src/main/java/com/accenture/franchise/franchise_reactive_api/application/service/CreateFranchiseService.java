package com.accenture.franchise.franchise_reactive_api.application.service;

import com.accenture.franchise.franchise_reactive_api.domain.model.Franchise;
import com.accenture.franchise.franchise_reactive_api.domain.repository.IFranchiseRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class CreateFranchiseService {

    public final IFranchiseRepository franchiseRepository;

    public Mono<Franchise> create(String name) {
        Franchise franchise = new Franchise(name, Collections.emptyList());
        return franchiseRepository.save(franchise);
    }
}
