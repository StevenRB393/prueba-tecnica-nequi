package com.application.prueba.controllers;

import com.application.prueba.dtos.FranchiseDTO;
import com.application.prueba.dtos.NewNameDTO;
import com.application.prueba.dtos.ProductWithScoreDTO;
import com.application.prueba.dtos.StoreDTO;
import com.application.prueba.entities.Franchise;
import com.application.prueba.mappers.FranchiseMapper;
import com.application.prueba.services.FranchiseService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("api/franchises")
@RequiredArgsConstructor
@Tag(name = "Franchises")
public class FranchiseController {

    private static final Logger logger = LoggerFactory.getLogger(FranchiseController.class);

    private final FranchiseService franchiseService;

    private final FranchiseMapper franchiseMapper;


    @GetMapping("/{franchiseId}")
    public Mono<ResponseEntity<FranchiseDTO>> getFranchiseById(@PathVariable String franchiseId) {
        logger.info("Request received to get franchise with ID: {}", franchiseId);
        return franchiseService.findFranchiseById(franchiseId)
                .map(franchise -> {
                    logger.info("Franchise found: {}", franchiseId);
                    return ResponseEntity.ok(franchiseMapper.INSTANCE.franchiseToFranchiseDTO(franchise));
                })
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }


    @GetMapping
    public Flux<ResponseEntity<FranchiseDTO>> getAllFranchises() {
        logger.info("Request received to get all franchises");
        return franchiseService.findAllFranchises()
                .map(franchise -> {
                    logger.info("Franchise found with ID: {}", franchise.getId());
                    return ResponseEntity.ok(franchiseMapper.franchiseToFranchiseDTO(franchise));
                })
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }


    @GetMapping("/{franchiseId}/products/most-stock")
    public Mono<ResponseEntity<List<ProductWithScoreDTO>>> getProductWithMostStockByStore(@PathVariable String franchiseId) {
        logger.info("Request received to get products with most stock for franchise ID: {}", franchiseId);

        return franchiseService.findProductWithMaxStockByStore(franchiseId)
                .defaultIfEmpty(Collections.emptyList()) // Si la lista está vacía, se retorna una lista vacía
                .flatMap(products -> {
                    if (products.isEmpty()) {
                        // Si la lista sigue vacía, retornamos NOT_FOUND
                        return Mono.just(ResponseEntity.notFound().build());
                    }
                    // Si hay productos, retornamos OK con la lista de productos
                    return Mono.just(ResponseEntity.ok(products));
                });
    }



    @PostMapping
    public Mono<ResponseEntity<FranchiseDTO>> saveFranchise(@RequestBody FranchiseDTO franchiseDTO) {
        logger.info("Request received to save a new franchise");
        return franchiseService.saveFranchise(franchiseDTO)
                .map(savedFranchise -> {
                    logger.info("Franchise saved with ID: {}", savedFranchise.getId());
                    return ResponseEntity.ok(FranchiseMapper.INSTANCE.franchiseToFranchiseDTO(savedFranchise));
                })
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }


    @PostMapping("/{franchiseId}/stores")
    public Mono<ResponseEntity<FranchiseDTO>> addStoreToFranchise(@PathVariable String franchiseId, @RequestBody StoreDTO storeDTO) {
        logger.info("Request received to add a store to franchise ID: {}", franchiseId);
        return franchiseService.saveStoreByFranchise(franchiseId, storeDTO)
                .map(franchise -> {
                    logger.info("Store added to franchise with ID: {}", franchiseId);
                    return ResponseEntity.ok(franchiseMapper.franchiseToFranchiseDTO(franchise));
                })
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }


    @PutMapping("/{franchiseId}")
    public Mono<ResponseEntity<Franchise>> updateFranchiseName(@PathVariable String franchiseId, @RequestBody NewNameDTO newNameDTO) {
        logger.info("Request received to update franchise name for ID: {}", franchiseId);
        return franchiseService.updateFranchiseName(franchiseId, newNameDTO)
                .map(updatedFranchise -> {
                    logger.info("Franchise name updated for ID: {}", franchiseId);
                    return ResponseEntity.ok().body(updatedFranchise);
                })
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }


    @DeleteMapping("{franchiseId}")
    public Mono<ResponseEntity<Void>> deleteFranchiseById(@PathVariable String franchiseId) {
        logger.info("Request received to delete franchise with ID: {}", franchiseId);
        return franchiseService.deleteFranchiseById(franchiseId)
                .then(Mono.fromRunnable(() -> logger.info("Franchise deleted successfully: {}", franchiseId)))
                .then(Mono.just(ResponseEntity.noContent().build()));
    }
}
