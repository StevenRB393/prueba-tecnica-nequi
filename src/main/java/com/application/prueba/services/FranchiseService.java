package com.application.prueba.services;

import com.application.prueba.dtos.*;
import com.application.prueba.models.Franchise;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.List;

public interface FranchiseService {

    Mono<Franchise> findFranchiseById(String franchiseId);

    Flux<Franchise> findAllFranchises();

    Mono<List<ProductWithScoreDTO>>findProductWithMaxStockByStore(String franchiseId);

    Mono<Franchise> saveFranchise(FranchiseDTO franchiseDTO);

    Mono<Franchise> saveStoreByFranchise(String franchiseId, StoreDTO storeDTO);

    Mono<Franchise> updateFranchiseName(String franchiseId, NewNameDTO newNameDTO);

    Mono<Void> deleteFranchiseById(String franchiseId);
}
