package com.application.prueba.controllers;

import com.application.prueba.dtos.*;
import com.application.prueba.entities.Franchise;
import com.application.prueba.entities.Product;
import com.application.prueba.entities.Store;
import com.application.prueba.mappers.FranchiseMapper;
import com.application.prueba.services.FranchiseService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.WARN)
public class FranchiseControllerTest {

    @Mock
    private FranchiseService franchiseService;

    @Mock
    private FranchiseMapper franchiseMapper;

    @InjectMocks
    private FranchiseController franchiseController;

    @Test
    void testGetFranchisieByIdController() {

        String franchiseId = "1";

        // Crear objetos para el test
        Franchise franchise = new Franchise();
        franchise.setId(franchiseId);
        franchise.setFranchiseName("Sample Franchise");
        franchise.setStoreList(new ArrayList<>()); // Lista vacía de tiendas para este caso

        FranchiseDTO franchiseDTO = new FranchiseDTO();
        franchiseDTO.setIdDTO(franchiseId);
        franchiseDTO.setFranchiseNameDTO("Sample Franchise");
        franchiseDTO.setStoreDTOList(new ArrayList<>()); // Lista vacía de tiendas para este caso

        // Mockear el comportamiento de los servicios y el mapper
        when(franchiseService.findFranchiseById(franchiseId)).thenReturn(Mono.just(franchise)); // Mock del servicio
        when(franchiseMapper.franchiseToFranchiseDTO(franchise)).thenReturn(franchiseDTO); // Mock del mapper

        // Ejecutar el controlador
        Mono<ResponseEntity<FranchiseDTO>> response = franchiseController.getFranchiseById(franchiseId);

        // Verificar con StepVerifier
        StepVerifier.create(response)
                .consumeNextWith(entity -> {
                    assertEquals(HttpStatus.OK, entity.getStatusCode());
                    assertNotNull(entity.getBody());
                    assertEquals("Sample Franchise", entity.getBody().getFranchiseNameDTO());
                    assertEquals(0, entity.getBody().getStoreDTOList().size());  // Verificar que la lista esté vacía
                })
                .verifyComplete();

    }

    @Test
    void testGetAllFranchisiesController() {
        Franchise franchise1 = new Franchise();
        franchise1.setId("1");
        franchise1.setFranchiseName("Franchise 1");
        franchise1.setStoreList(new ArrayList<>());

        Franchise franchise2 = new Franchise();
        franchise2.setId("2");
        franchise2.setFranchiseName("Franchise 2");
        franchise2.setStoreList(new ArrayList<>());

        FranchiseDTO franchiseDTO1 = new FranchiseDTO();
        franchiseDTO1.setIdDTO("1");
        franchiseDTO1.setFranchiseNameDTO("Franchise 1");
        franchiseDTO1.setStoreDTOList(new ArrayList<>());

        FranchiseDTO franchiseDTO2 = new FranchiseDTO();
        franchiseDTO2.setIdDTO("2");
        franchiseDTO2.setFranchiseNameDTO("Franchise 2");
        franchiseDTO2.setStoreDTOList(new ArrayList<>());

        // Mockear el comportamiento de los servicios y el mapper
        when(franchiseService.findAllFranchises()).thenReturn(Flux.just(franchise1, franchise2)); // Mock del servicio
        when(franchiseMapper.franchiseToFranchiseDTO(franchise1)).thenReturn(franchiseDTO1); // Mock del mapper
        when(franchiseMapper.franchiseToFranchiseDTO(franchise2)).thenReturn(franchiseDTO2); // Mock del mapper

        // Ejecutar el controlador
        Flux<ResponseEntity<FranchiseDTO>> response = franchiseController.getAllFranchises();

        // Verificar con StepVerifier
        StepVerifier.create(response)
                .consumeNextWith(entity -> {
                    assertEquals(HttpStatus.OK, entity.getStatusCode());
                    assertNotNull(entity.getBody());
                    assertEquals("Franchise 1", entity.getBody().getFranchiseNameDTO());
                })
                .consumeNextWith(entity -> {
                    assertEquals(HttpStatus.OK, entity.getStatusCode());
                    assertNotNull(entity.getBody());
                    assertEquals("Franchise 2", entity.getBody().getFranchiseNameDTO());
                })
                .verifyComplete();
    }

    @Test
    void testGetProductWithMostStockByStoreController() {

        String franchiseId = "1";

        // Crear datos para el test
        ProductWithScoreDTO productWithScoreDTO1 = new ProductWithScoreDTO("1", "Product A", 50); // Producto 1
        ProductWithScoreDTO productWithScoreDTO2 = new ProductWithScoreDTO("2", "Product B", 100); // Producto 2

        // Crear lista de productos con puntaje
        List<ProductWithScoreDTO> productList = Arrays.asList(productWithScoreDTO1, productWithScoreDTO2);

        // Mockear el comportamiento del servicio
        when(franchiseService.findProductWithMaxStockByStore(franchiseId)).thenReturn(Mono.just(productList)); // Retorna la lista de productos

        // Ejecutar el controlador
        Mono<ResponseEntity<List<ProductWithScoreDTO>>> response = franchiseController.getProductWithMostStockByStore(franchiseId);

        // Verificar con StepVerifier
        StepVerifier.create(response)
                .consumeNextWith(entity -> {
                    assertEquals(HttpStatus.OK, entity.getStatusCode());
                    assertNotNull(entity.getBody());
                    assertEquals(2, entity.getBody().size()); // Comprobar que la lista tiene 2 productos
                    assertEquals("Product B", entity.getBody().get(1).getProductNameDTO()); // Verificar que el producto con más stock está en la segunda posición
                    assertEquals(100, entity.getBody().get(1).getStockDTO()); // Verificar que el producto con más stock tiene 100
                })
                .verifyComplete();
    }

    @Test
    void testSaveFranchisieController() {
        String franchiseId = "1";
        FranchiseDTO franchiseDTO = new FranchiseDTO();
        franchiseDTO.setFranchiseNameDTO("Franchise Example");

        Franchise savedFranchise = new Franchise(franchiseId, "Franchise Example", new ArrayList<>());
        FranchiseDTO savedFranchiseDTO = new FranchiseDTO(franchiseId, "Franchise Example", new ArrayList<>());

        // Mockear el comportamiento
        when(franchiseService.saveFranchise(franchiseDTO)).thenReturn(Mono.just(savedFranchise));
        when(franchiseMapper.franchiseToFranchiseDTO(savedFranchise)).thenReturn(savedFranchiseDTO);

        // Ejecutar el controlador
        Mono<ResponseEntity<FranchiseDTO>> response = franchiseController.saveFranchise(franchiseDTO);

        // Verificar con StepVerifier
        StepVerifier.create(response)
                .consumeNextWith(entity -> {
                    assertEquals(HttpStatus.OK, entity.getStatusCode());
                    assertNotNull(entity.getBody());
                    assertEquals(franchiseId, entity.getBody().getIdDTO());
                    assertEquals("Franchise Example", entity.getBody().getFranchiseNameDTO());
                })
                .verifyComplete();
    }

    @Test
    void testAddStoreToFranchisieController() {
        String franchiseId = "1";
        StoreDTO storeDTO = new StoreDTO("store-1", "Store Example", new ArrayList<>());  // Asumiendo lista vacía de productos

        // Franchise antes de agregar la tienda
        Franchise franchiseBefore = new Franchise(franchiseId, "Franchise Example", new ArrayList<>());

        // Crear una tienda que será agregada a la franquicia
        Product product1 = new Product("product-1", "Product Example", 100);  // Crear un producto ejemplo
        List<Product> products = new ArrayList<>();
        products.add(product1);
        Store store = new Store("store-1", "Store Example", products);  // Asumiendo que Store tiene productos
        franchiseBefore.getStoreList().add(store);  // Agregar la tienda a la franquicia

        // Convertir los productos a ProductDTO
        List<ProductDTO> productDTOList = products.stream()
                .map(p -> new ProductDTO(p.getId(), p.getProductName(), p.getStock()))  // Convertir Product a ProductDTO
                .collect(Collectors.toList());

        // Convertir la tienda a StoreDTO
        List<StoreDTO> storeDTOList = franchiseBefore.getStoreList().stream()
                .map(s -> new StoreDTO(s.getId(), s.getStoreName(), productDTOList)) // Convertir cada Store a StoreDTO
                .collect(Collectors.toList());

        // Crear el FranchiseDTO con la lista de StoreDTO
        FranchiseDTO updatedFranchiseDTO = new FranchiseDTO(franchiseId, "Franchise Example", storeDTOList);

        // Mockear el comportamiento
        when(franchiseService.saveStoreByFranchise(franchiseId, storeDTO)).thenReturn(Mono.just(franchiseBefore));
        when(franchiseMapper.franchiseToFranchiseDTO(franchiseBefore)).thenReturn(updatedFranchiseDTO);

        // Ejecutar el controlador
        Mono<ResponseEntity<FranchiseDTO>> response = franchiseController.addStoreToFranchise(franchiseId, storeDTO);

        // Verificar con StepVerifier
        StepVerifier.create(response)
                .consumeNextWith(entity -> {
                    assertEquals(HttpStatus.OK, entity.getStatusCode());
                    assertNotNull(entity.getBody());
                    assertEquals(franchiseId, entity.getBody().getIdDTO());
                    assertEquals("Franchise Example", entity.getBody().getFranchiseNameDTO());
                    assertEquals(1, entity.getBody().getStoreDTOList().size());
                    StoreDTO storeDTOResponse = entity.getBody().getStoreDTOList().get(0);
                    assertEquals("Store Example", storeDTOResponse.getStoreNameDTO());
                    assertEquals("store-1", storeDTOResponse.getIdDTO());
                    assertEquals(1, storeDTOResponse.getProductDTOList().size());
                    assertEquals("product-1", storeDTOResponse.getProductDTOList().get(0).getIdDTO());
                    assertEquals("Product Example", storeDTOResponse.getProductDTOList().get(0).getProductNameDTO());
                })
                .verifyComplete();

    }

    @Test
    void testUpdateFranchisieNameController() {
        String franchiseId = "1";
        String newName = "Updated Franchise Name";
        NewNameDTO newNameDTO = new NewNameDTO(newName);

        // Franchise antes de la actualización
        Franchise franchiseBefore = new Franchise(franchiseId, "Old Franchise Name", new ArrayList<>());

        // Franchise después de la actualización
        Franchise franchiseAfter = new Franchise(franchiseId, newName, new ArrayList<>());

        // Mockear el comportamiento del servicio
        when(franchiseService.updateFranchiseName(franchiseId, newNameDTO)).thenReturn(Mono.just(franchiseAfter));

        // Ejecutar el controlador
        Mono<ResponseEntity<Franchise>> response = franchiseController.updateFranchiseName(franchiseId, newNameDTO);

        // Verificar con StepVerifier
        StepVerifier.create(response)
                .consumeNextWith(entity -> {
                    // Verificar que el estado sea OK (200)
                    assertEquals(HttpStatus.OK, entity.getStatusCode());

                    // Verificar que el body no es null
                    assertNotNull(entity.getBody());

                    // Verificar que el ID de la franquicia es el mismo
                    assertEquals(franchiseId, entity.getBody().getId());

                    // Verificar que el nombre de la franquicia es el nuevo nombre
                    assertEquals(newName, entity.getBody().getFranchiseName());
                })
                .verifyComplete();
    }

    @Test
    void testDeleteFranchisieByIdController() {
        String franchiseId = "1";

        // Mockear el comportamiento del servicio
        when(franchiseService.deleteFranchiseById(franchiseId)).thenReturn(Mono.empty());

        // Ejecutar el controlador
        Mono<ResponseEntity<Void>> response = franchiseController.deleteFranchiseById(franchiseId);

        // Verificar con StepVerifier
        StepVerifier.create(response)
                .consumeNextWith(entity -> {
                    // Verificar que el estado sea NO_CONTENT (204)
                    assertEquals(HttpStatus.NO_CONTENT, entity.getStatusCode());

                    // Verificar que el body esté vacío
                    assertNull(entity.getBody());
                })
                .verifyComplete();

        // Verificar que el método del servicio fue llamado con el ID correcto
        verify(franchiseService, times(1)).deleteFranchiseById(franchiseId);
    }
}
