package com.application.prueba.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import com.application.prueba.dtos.NewNameDTO;
import com.application.prueba.dtos.ProductDTO;
import com.application.prueba.dtos.StoreDTO;
import com.application.prueba.models.Product;
import com.application.prueba.models.Store;
import com.application.prueba.mappers.ProductMapper;
import com.application.prueba.repositories.StoreRepository;
import com.application.prueba.services.impl.StoreServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StoreServiceTest {

    @InjectMocks
    private StoreServiceImpl storeService;

    @Mock
    private StoreRepository storeRepository;

    @Test
    void testFindStoreById() {

        String storeId = "store1";
        Store store = Store.builder()
                .id(storeId)
                .storeName("Store One")
                .productList(new ArrayList<>())
                .build();

        when(storeRepository.findById(storeId)).thenReturn(Mono.just(store));

        Mono<Store> result = storeService.findStoreById(storeId);

        StepVerifier.create(result)
                .expectNextMatches(s -> s.getId().equals(storeId) && s.getStoreName().equals("Store One"))

                .verifyComplete();
    }

    @Test
    void testFindAllStores() {

        Store store1 = Store.builder()
                .id("store1")
                .storeName("Store One")
                .productList(new ArrayList<>())
                .build();

        Store store2 = Store.builder()
                .id("store2")
                .storeName("Store Two")
                .productList(new ArrayList<>())
                .build();

        when(storeRepository.findAll()).thenReturn(Flux.just(store1, store2));

        Flux<Store> result = storeService.findAllStores();

        StepVerifier.create(result)
                .expectNext(store1, store2)

                .verifyComplete();
    }

    @Test
    void testSaveStore() {

        List<ProductDTO> productDTOList = Collections.singletonList(
                ProductDTO.builder()
                        .idDTO("product1")
                        .productNameDTO("Product One")
                        .stockDTO(10)
                        .build()
        );

        StoreDTO storeDTO = StoreDTO.builder()
                .idDTO("store1")
                .storeNameDTO("Store One")
                .productDTOList(productDTOList)
                .build();

        List<Product> productList = productDTOList.stream()
                .map(ProductMapper.INSTANCE::productDTOToProduct)
                .toList();

        Store store = new Store(storeDTO.getIdDTO(), storeDTO.getStoreNameDTO(), productList);

        when(storeRepository.save(any(Store.class))).thenReturn(Mono.just(store));

        Mono<StoreDTO> result = storeService.saveStore(storeDTO);

        StepVerifier.create(result)
                .expectNextMatches(s -> s.getIdDTO().equals("store1") && s.getStoreNameDTO().equals("Store One"))

                .verifyComplete();
    }

    @Test
    void testSaveProductToStore() {

        String storeId = "store1";

        ProductDTO productDTO = ProductDTO.builder()
                .idDTO("product1")
                .productNameDTO("Sample Product")
                .stockDTO(10)
                .build();

        Store store = Store.builder()
                .id(storeId)
                .storeName("Sample Store")
                .productList(new ArrayList<>())
                .build();

        when(storeRepository.findById(storeId)).thenReturn(Mono.just(store));
        when(storeRepository.save(any(Store.class))).thenReturn(Mono.just(store));

        Mono<Store> result = storeService.saveProductToStore(storeId, productDTO);

        StepVerifier.create(result)
                .expectNext(store)
                .expectComplete()
                .verify();

        verify(storeRepository, times(1)).findById(storeId);
        verify(storeRepository, times(1)).save(store);
    }

    @Test
    void testDeleteProductFromStore() {

        String storeId = "store1";
        String productId = "product1";

        List<Product> products = Stream.of(
                new Product("product1", "Product One", 10),
                new Product("product2", "Product Two", 5)
        ).collect(Collectors.toList());

        Store store = Store.builder()
                .id(storeId)
                .storeName("Store One")
                .productList(products)
                .build();

        when(storeRepository.findById(storeId)).thenReturn(Mono.just(store));

        when(storeRepository.save(any(Store.class))).thenAnswer(invocation -> {
            Store updatedStore = invocation.getArgument(0);
            updatedStore.getProductList().removeIf(product -> product.getId().equals(productId));
            return Mono.just(updatedStore);
        });

        Mono<Store> result = storeService.deleteProductFromStore(storeId, productId);

        StepVerifier.create(result)
                .assertNext(updatedStore -> {
                    assertEquals(1, updatedStore.getProductList().size());
                    assertTrue(updatedStore.getProductList().stream().noneMatch(p -> p.getId().equals(productId)));
                })

                .verifyComplete();

        verify(storeRepository).findById(storeId);
        verify(storeRepository).save(any(Store.class));
    }

    @Test
    void testUpdateStoreName() {

        String storeId = "store1";

        NewNameDTO newNameDTO = NewNameDTO.builder()
                .newNameDTO("Updated Store Name")
                .build();

        Store originalStore = Store.builder()
                .id(storeId)
                .storeName("Original Store Name")
                .productList(new ArrayList<>())
                .build();

        Store updatedStore = Store.builder()
                .id(storeId)
                .storeName(newNameDTO.getNewNameDTO())
                .productList(new ArrayList<>())
                .build();

        when(storeRepository.findById(storeId)).thenReturn(Mono.just(originalStore));

        when(storeRepository.save(any(Store.class))).thenReturn(Mono.just(updatedStore));

        Mono<Store> result = storeService.updateStoreName(storeId, newNameDTO);

        StepVerifier.create(result)
                .assertNext(store -> {
                    assertEquals(newNameDTO.getNewNameDTO(), store.getStoreName()); // Verifica que el nombre ha sido actualizado
                    assertEquals(storeId, store.getId()); // Verifica que el ID no cambia
                })

                .verifyComplete();

        verify(storeRepository).findById(storeId);
        verify(storeRepository).save(any(Store.class));
    }

    @Test
    void testDeleteStoreById() {

        String storeId = "store1";

        when(storeRepository.deleteById(storeId)).thenReturn(Mono.empty());

        Mono<Void> result = storeService.deleteStoreById(storeId);

        StepVerifier.create(result)

                .verifyComplete();

        verify(storeRepository, times(1)).deleteById(storeId);
    }
}
