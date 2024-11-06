package com.application.prueba.repositories;

import com.application.prueba.entities.Franchise;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FranchiseRepository extends ReactiveMongoRepository<Franchise, String> {

}
