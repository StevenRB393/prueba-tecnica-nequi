package com.application.prueba.services.impl;

import com.application.prueba.dtos.*;
import com.application.prueba.entities.Franchise;
import com.application.prueba.entities.Product;
import com.application.prueba.entities.Store;
import com.application.prueba.exceptions.NotFoundException;
import com.application.prueba.mappers.FranchiseMapper;
import com.application.prueba.mappers.StoreMapper;
import com.application.prueba.repositories.FranchiseRepository;
import com.application.prueba.services.FranchiseService;
import com.application.prueba.utils.FranchiseValidations;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FranchiseServiceImpl implements FranchiseService {

    private final FranchiseMapper franchiseMapper;

    private final FranchiseRepository franchiseRepository;

    @Override
    public Mono<Franchise> findFranchiseById(String franchiseId) {

        FranchiseValidations.validateFranchiseId(franchiseId);

        return franchiseRepository.findById(franchiseId);
    }

    @Override
    public Flux<Franchise> findAllFranchises() {

        return franchiseRepository.findAll();
    }

    @Override
    public Mono<List<ProductWithScoreDTO>> findProductWithMaxStockByStore(String franchiseId) {

        return franchiseRepository.findById(franchiseId)
                .flatMap(franchise -> {
                    List<ProductWithScoreDTO> productsWithMaxStock = new ArrayList<>();

                    for (Store store : franchise.getStoreList()) {
                        Optional<Product> maxStockProduct = store.getProductList().stream()
                                .max(Comparator.comparingInt(Product::getStock));

                        maxStockProduct.ifPresent(product -> {
                            productsWithMaxStock.add(new ProductWithScoreDTO(product.getProductName(), store.getStoreName(), product.getStock()));
                        });
                    }

                    return Mono.just(productsWithMaxStock);
                });
    }

    @Override
    public Mono<Franchise> saveFranchise(FranchiseDTO franchiseDTO) {

        FranchiseValidations.validateFranchiseDTO(franchiseDTO);

        Franchise franchise = new Franchise();

        franchise.setId(franchiseDTO.getIdDTO());
        franchise.setFranchiseName(franchiseDTO.getFranchiseNameDTO());

        List<Store> stores = new ArrayList<>();

        if (franchiseDTO.getStoreDTOList() != null) {
            for (StoreDTO storeDTO : franchiseDTO.getStoreDTOList()) {

                FranchiseValidations.validateStoreDTO(storeDTO);

                Store store = new Store();

                store.setId(storeDTO.getIdDTO());
                store.setStoreName(storeDTO.getStoreNameDTO());

                List<Product> products = new ArrayList<>();

                if (storeDTO.getProductDTOList() != null) {
                    for (ProductDTO productDTO : storeDTO.getProductDTOList()) {

                        Product product = new Product();

                        product.setId(productDTO.getIdDTO());
                        product.setProductName(productDTO.getProductNameDTO());
                        product.setStock(productDTO.getStockDTO());

                        products.add(product);
                    }
                }

                store.setProductList(products);
                stores.add(store);

            }
        }

        franchise.setStoreList(stores);

        return franchiseRepository.save(franchise);
    }

    @Override
    public Mono<Franchise> saveStoreByFranchise(String franchiseId, StoreDTO storeDTO) {

        FranchiseValidations.validateFranchiseId(franchiseId);

        FranchiseValidations.validateStoreDTO(storeDTO);

        return franchiseRepository.findById(franchiseId)
                .flatMap(franchise -> {

                    Store store = new Store();

                    store.setId(storeDTO.getIdDTO());
                    store.setStoreName(storeDTO.getStoreNameDTO());

                    List<Product> products = new ArrayList<>();

                    if (storeDTO.getProductDTOList() != null) {
                        for (ProductDTO productDTO : storeDTO.getProductDTOList()) {

                            Product product = new Product();

                            product.setId(productDTO.getIdDTO());
                            product.setProductName(productDTO.getProductNameDTO());
                            product.setStock(productDTO.getStockDTO());

                            products.add(product);
                        }
                    }

                    store.setProductList(products);
                    franchise.getStoreList().add(store);

                    return franchiseRepository.save(franchise);

                });
    }

    @Override
    public Mono<Franchise> updateFranchiseName(String franchiseId, NewNameDTO newNameDTO) {

        FranchiseValidations.validateFranchiseId(franchiseId);

        FranchiseValidations.validateNewNameDTO(newNameDTO);

       return franchiseRepository.findById(franchiseId)
               .flatMap(oldName -> {
                   oldName.setFranchiseName(newNameDTO.getNewNameDTO());

                   return franchiseRepository.save(oldName);
               });
    }

    @Override
    public Mono<Void> deleteFranchiseById(String franchiseId) {

        FranchiseValidations.validateFranchiseId(franchiseId);

        return franchiseRepository.deleteById(franchiseId);
    }
}
