package com.application.prueba.services;

import com.application.prueba.dtos.NewNameDTO;
import com.application.prueba.dtos.ProductDTO;
import com.application.prueba.dtos.StoreDTO;
import com.application.prueba.entities.Store;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface StoreService {

    Mono<Store> findStoreById(String storeId);

    Flux<Store> findAllStores();

    Mono<StoreDTO> saveStore(StoreDTO storeDTO);

    Mono<Store> saveProductToStore(String storeId, ProductDTO productDTO);

    Mono<Store> deleteProductFromStore(String storeId, String productId);

    Mono<Store> updateStoreName(String storeId, NewNameDTO newNameDTO);

    Mono<Void> deleteStoreById(String storeId);
}
