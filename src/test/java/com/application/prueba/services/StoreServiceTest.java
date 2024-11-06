package com.application.prueba.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import com.application.prueba.dtos.NewNameDTO;
import com.application.prueba.dtos.NewStockDTO;
import com.application.prueba.dtos.ProductDTO;
import com.application.prueba.dtos.StoreDTO;
import com.application.prueba.entities.Product;
import com.application.prueba.entities.Store;
import com.application.prueba.mappers.ProductMapper;
import com.application.prueba.mappers.ProductMapperImpl;
import com.application.prueba.mappers.StoreMapper;
import com.application.prueba.repositories.ProductRepository;
import com.application.prueba.repositories.StoreRepository;
import com.application.prueba.services.impl.StoreServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import static reactor.core.publisher.Mono.when;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StoreServiceTest {

    @InjectMocks
    private StoreServiceImpl storeService;

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;


    @Test
    void testFindStoreById() {

        String storeId = "store1";
        Store store = new Store(storeId, "Store One", new ArrayList<>());

        when(storeRepository.findById(storeId)).thenReturn(Mono.just(store));

        Mono<Store> result = storeService.findStoreById(storeId);

        StepVerifier.create(result)
                .expectNextMatches(s -> s.getId().equals(storeId) && s.getStoreName().equals("Store One"))
                .verifyComplete();
    }

    @Test
    void testFindAllStores() {
        Store store1 = new Store("store1", "Store One", new ArrayList<>());
        Store store2 = new Store("store2", "Store Two", new ArrayList<>());

        when(storeRepository.findAll()).thenReturn(Flux.just(store1, store2));

        Flux<Store> result = storeService.findAllStores();

        StepVerifier.create(result)
                .expectNext(store1, store2)
                .verifyComplete();
    }

    @Test
    void testSaveStore() {

        List<ProductDTO> productDTOList = new ArrayList<>(); // Inicializa la lista de productos DTO
        productDTOList.add(new ProductDTO("product1", "Product One", 10)); // Agrega un producto de ejemplo

        StoreDTO storeDTO = new StoreDTO("store1", "Store One", productDTOList);

        // Mapeo de ProductDTO a Product
        List<Product> productList = productDTOList.stream()
                .map(ProductMapper.INSTANCE::productDTOToProduct) // Convierte cada ProductDTO a Product
                .collect(Collectors.toList());

        Store store = new Store(storeDTO.getIdDTO(), storeDTO.getStoreNameDTO(), productList); // Usa la lista convertida

        // Simula el comportamiento del repositorio
        when(storeRepository.save(any(Store.class))).thenReturn(Mono.just(store));

        Mono<StoreDTO> result = storeService.saveStore(storeDTO);

        // Verifica el resultado
        StepVerifier.create(result)
                .expectNextMatches(s -> s.getIdDTO().equals("store1") && s.getStoreNameDTO().equals("Store One"))
                .verifyComplete();
    }

    @Test
    void testSaveProductToStore() {
        String storeId = "store1";
        ProductDTO productDTO = new ProductDTO();
        productDTO.setIdDTO("product1");
        productDTO.setProductNameDTO("Sample Product");
        productDTO.setStockDTO(10);

        Store store = new Store();
        store.setId(storeId);
        store.setStoreName("Sample Store");
        store.setProductList(new ArrayList<>());

        when(storeRepository.findById(storeId)).thenReturn(Mono.just(store)); // Simulando que se encuentra la tienda.
        when(storeRepository.save(any(Store.class))).thenReturn(Mono.just(store)); // Simulando el guardado de la tienda.

        // Act
        Mono<Store> result = storeService.saveProductToStore(storeId, productDTO);

        // Assert
        StepVerifier.create(result)
                .expectNext(store) // Verifica que el resultado sea la tienda actualizada
                .expectComplete() // Verifica que se complete sin errores
                .verify();

        // Verificar que el método findById y save fueron llamados correctamente.
        verify(storeRepository, times(1)).findById(storeId);
        verify(storeRepository, times(1)).save(store);
    }

    @Test
    void testDeleteProductFromStore() {

        String storeId = "store1";
        String productId = "product1";

        // Configuramos el store con dos productos en la lista
        List<Product> products = new ArrayList<>();
        products.add(new Product(productId, "Product One", 10));
        products.add(new Product("product2", "Product Two", 5));

        Store store = new Store(storeId, "Store One", products);

        // Mock para encontrar la tienda por ID
        when(storeRepository.findById(storeId)).thenReturn(Mono.just(store));

        // Mock para guardar la tienda actualizada sin el producto eliminado
        when(storeRepository.save(any(Store.class))).thenAnswer(invocation -> {
            Store updatedStore = invocation.getArgument(0);
            updatedStore.getProductList().removeIf(product -> product.getId().equals(productId));
            return Mono.just(updatedStore);
        });

        // Act
        Mono<Store> result = storeService.deleteProductFromStore(storeId, productId);

        // Assert
        StepVerifier.create(result)
                .assertNext(updatedStore -> {
                    assertEquals(1, updatedStore.getProductList().size()); // Verifica que hay solo un producto
                    assertTrue(updatedStore.getProductList().stream().noneMatch(p -> p.getId().equals(productId))); // Verifica que el producto fue eliminado
                })
                .verifyComplete();

        // Verificación de que se llamaron los métodos esperados
        verify(storeRepository).findById(storeId);
        verify(storeRepository).save(any(Store.class));

    }

    @Test
    void testUpdateStoreName() {

        String storeId = "store1";
        NewNameDTO newNameDTO = new NewNameDTO("Updated Store Name"); // Usamos el DTO en lugar de un String

        // Creamos una tienda original
        Store originalStore = new Store(storeId, "Original Store Name", new ArrayList<>());
        Store updatedStore = new Store(storeId, newNameDTO.getNewNameDTO(), new ArrayList<>()); // Tienda con el nombre actualizado

        // Mock para encontrar la tienda por ID
        when(storeRepository.findById(storeId)).thenReturn(Mono.just(originalStore));

        // Mock para guardar la tienda con el nombre actualizado
        when(storeRepository.save(any(Store.class))).thenReturn(Mono.just(updatedStore));

        // Act
        Mono<Store> result = storeService.updateStoreName(storeId, newNameDTO);

        // Assert
        StepVerifier.create(result)
                .assertNext(store -> {
                    assertEquals(newNameDTO.getNewNameDTO(), store.getStoreName()); // Verifica que el nombre ha sido actualizado
                    assertEquals(storeId, store.getId()); // Verifica que el ID no cambia
                })
                .verifyComplete();

        // Verificación de que se llamaron los métodos esperados
        verify(storeRepository).findById(storeId);
        verify(storeRepository).save(any(Store.class));

    }

    @Test
    void testDeleteStoreById() {

        String storeId = "store1";

        // Mock para el comportamiento de deleteById
        when(storeRepository.deleteById(storeId)).thenReturn(Mono.empty());

        // Act
        Mono<Void> result = storeService.deleteStoreById(storeId);

        // Assert
        StepVerifier.create(result)
                .verifyComplete(); // Verifica que el resultado sea completo (sin errores)

        // Verifica que se haya llamado deleteById con el ID correcto
        verify(storeRepository, times(1)).deleteById(storeId);

    }

}
