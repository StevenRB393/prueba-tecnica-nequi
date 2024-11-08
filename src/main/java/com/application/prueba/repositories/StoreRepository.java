package com.application.prueba.repositories;

import com.application.prueba.models.Store;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreRepository extends ReactiveMongoRepository<Store, String> {
}
