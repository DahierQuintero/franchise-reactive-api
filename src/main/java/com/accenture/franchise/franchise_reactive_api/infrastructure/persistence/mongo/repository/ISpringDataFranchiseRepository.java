package com.accenture.franchise.franchise_reactive_api.infrastructure.persistence.mongo.repository;

import com.accenture.franchise.franchise_reactive_api.infrastructure.persistence.mongo.document.FranchiseDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ISpringDataFranchiseRepository extends ReactiveMongoRepository<FranchiseDocument, String> {
}
