package com.accenture.franchise.franchise_reactive_api.infrastructure.persistence.mongo;

import com.accenture.franchise.franchise_reactive_api.domain.model.Franchise;
import com.accenture.franchise.franchise_reactive_api.domain.repository.IFranchiseRepository;
import com.accenture.franchise.franchise_reactive_api.infrastructure.persistence.mongo.mapper.FranchiseMongoMapper;
import com.accenture.franchise.franchise_reactive_api.infrastructure.persistence.mongo.repository.ISpringDataFranchiseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class FranchiseMongoRepositoryAdapter implements IFranchiseRepository {

    private final ISpringDataFranchiseRepository repository;

    @Override
    public Mono<Franchise> save(Franchise franchise) {
        return repository
                .save(FranchiseMongoMapper.toDocument(franchise))
                .map(FranchiseMongoMapper::toDomain);
    }

    @Override
    public Mono<Franchise> findById(String id) {
        return repository.findById(id)
                .map(FranchiseMongoMapper::toDomain);
    }

    @Override
    public Flux<Franchise> findAll() {
        return repository.findAll()
                .map(FranchiseMongoMapper::toDomain);
    }
}
