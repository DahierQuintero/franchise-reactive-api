package com.accenture.franchise.franchise_reactive_api.domain.repository;

import com.accenture.franchise.franchise_reactive_api.domain.model.Franchise;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IFranchiseRepository {

    Mono<Franchise> save(Franchise franchise);

    Mono<Franchise> findById(String id);

    Flux<Franchise> findAll();

}
