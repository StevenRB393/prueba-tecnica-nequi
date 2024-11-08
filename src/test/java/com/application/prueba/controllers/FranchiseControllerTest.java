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
import static org.junit.jupiter.api.Assertions.*;
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
    void testGetFranchiseByIdController() {

        String franchiseId = "1";

        Franchise franchise = Franchise.builder()
                .id(franchiseId)
                .franchiseName("Sample Franchise")
                .storeList(new ArrayList<>())
                .build();

        FranchiseDTO franchiseDTO = FranchiseDTO.builder()
                .idDTO(franchiseId)
                .franchiseNameDTO("Sample Franchise")
                .storeDTOList(new ArrayList<>())
                .build();

        when(franchiseService.findFranchiseById(franchiseId)).thenReturn(Mono.just(franchise));
        when(franchiseMapper.franchiseToFranchiseDTO(franchise)).thenReturn(franchiseDTO);

        Mono<ResponseEntity<FranchiseDTO>> response = franchiseController.getFranchiseById(franchiseId);

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

        Franchise franchise1 = Franchise.builder()
                .id("1")
                .franchiseName("Franchise 1")
                .storeList(new ArrayList<>())
                .build();

        Franchise franchise2 = Franchise.builder()
                .id("2")
                .franchiseName("Franchise 2")
                .storeList(new ArrayList<>())
                .build();

        FranchiseDTO franchiseDTO1 = FranchiseDTO.builder()
                .idDTO("1")
                .franchiseNameDTO("Franchise 1")
                .storeDTOList(new ArrayList<>())
                .build();

        FranchiseDTO franchiseDTO2 = FranchiseDTO.builder()
                .idDTO("2")
                .franchiseNameDTO("Franchise 2")
                .storeDTOList(new ArrayList<>())
                .build();

        when(franchiseService.findAllFranchises()).thenReturn(Flux.just(franchise1, franchise2)); // Mock del servicio
        when(franchiseMapper.franchiseToFranchiseDTO(franchise1)).thenReturn(franchiseDTO1); // Mock del mapper
        when(franchiseMapper.franchiseToFranchiseDTO(franchise2)).thenReturn(franchiseDTO2); // Mock del mapper

        Flux<ResponseEntity<FranchiseDTO>> response = franchiseController.getAllFranchises();

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

        ProductWithScoreDTO productWithScoreDTO1 = ProductWithScoreDTO.builder()
                .productNameDTO("Product A")
                .stockDTO(50)
                .build();

        ProductWithScoreDTO productWithScoreDTO2 = ProductWithScoreDTO.builder()
                .productNameDTO("Product B")
                .stockDTO(100)
                .build();

        List<ProductWithScoreDTO> productList = Arrays.asList(productWithScoreDTO1, productWithScoreDTO2);

        when(franchiseService.findProductWithMaxStockByStore(franchiseId)).thenReturn(Mono.just(productList));

        Mono<ResponseEntity<List<ProductWithScoreDTO>>> response = franchiseController.getProductWithMostStockByStore(franchiseId);

        StepVerifier.create(response)
                .consumeNextWith(entity -> {
                    assertEquals(HttpStatus.OK, entity.getStatusCode());
                    assertNotNull(entity.getBody());
                    assertEquals(2, entity.getBody().size());
                    assertEquals("Product B", entity.getBody().get(1).getProductNameDTO());
                    assertEquals(100, entity.getBody().get(1).getStockDTO());
                })

                .verifyComplete();
    }

    @Test
    void testSaveFranchisieController() {

        String franchiseId = "1";

        FranchiseDTO franchiseDTO = FranchiseDTO.builder()
                .franchiseNameDTO("Franchise Example")
                .build();

        Franchise savedFranchise = Franchise.builder()
                .id(franchiseId)
                .franchiseName("Franchise Example")
                .storeList(new ArrayList<>())
                .build();

        FranchiseDTO savedFranchiseDTO = FranchiseDTO.builder()
                .idDTO(franchiseId)
                .franchiseNameDTO("Franchise Example")
                .storeDTOList(new ArrayList<>())
                .build();

        when(franchiseService.saveFranchise(franchiseDTO)).thenReturn(Mono.just(savedFranchise));
        when(franchiseMapper.franchiseToFranchiseDTO(savedFranchise)).thenReturn(savedFranchiseDTO);

        Mono<ResponseEntity<FranchiseDTO>> response = franchiseController.saveFranchise(franchiseDTO);

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

        StoreDTO storeDTO = StoreDTO.builder()
                .idDTO("store-1")
                .storeNameDTO("Store Example")
                .productDTOList(new ArrayList<>())
                .build();

        Franchise franchiseBefore = Franchise.builder()
                .id(franchiseId)
                .franchiseName("Franchise Example")
                .storeList(new ArrayList<>())
                .build();

        Product product1 = Product.builder()
                .id("product-1")
                .productName("Product Example")
                .stock(100)
                .build();

        List<Product> products = new ArrayList<>();
        products.add(product1);

        Store store = Store.builder()
                .id("store-1")
                .storeName("Store Example")
                .productList(products)
                .build();

        franchiseBefore.getStoreList().add(store);

        List<ProductDTO> productDTOList = products.stream()
                .map(p -> ProductDTO.builder()
                        .idDTO(p.getId())
                        .productNameDTO(p.getProductName())
                        .stockDTO(p.getStock())
                        .build())
                .toList();

        List<StoreDTO> storeDTOList = franchiseBefore.getStoreList().stream()
                .map(s -> StoreDTO.builder()
                        .idDTO(s.getId())
                        .storeNameDTO(s.getStoreName())
                        .productDTOList(productDTOList)
                        .build())
                .toList();

        FranchiseDTO updatedFranchiseDTO = FranchiseDTO.builder()
                .idDTO(franchiseId)
                .franchiseNameDTO("Franchise Example")
                .storeDTOList(storeDTOList)
                .build();

        when(franchiseService.saveStoreByFranchise(franchiseId, storeDTO)).thenReturn(Mono.just(franchiseBefore));
        when(franchiseMapper.franchiseToFranchiseDTO(franchiseBefore)).thenReturn(updatedFranchiseDTO);

        Mono<ResponseEntity<FranchiseDTO>> response = franchiseController.addStoreToFranchise(franchiseId, storeDTO);

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

        NewNameDTO newNameDTO = NewNameDTO.builder()
                .newNameDTO(newName)
                .build();

        Franchise franchiseAfter = Franchise.builder()
                .id(franchiseId)
                .franchiseName(newName)
                .storeList(new ArrayList<>())
                .build();

        when(franchiseService.updateFranchiseName(franchiseId, newNameDTO)).thenReturn(Mono.just(franchiseAfter));

        Mono<ResponseEntity<Franchise>> response = franchiseController.updateFranchiseName(franchiseId, newNameDTO);

        StepVerifier.create(response)
                .consumeNextWith(entity -> {
                    assertEquals(HttpStatus.OK, entity.getStatusCode());
                    assertNotNull(entity.getBody());
                    assertEquals(franchiseId, entity.getBody().getId());
                    assertEquals(newName, entity.getBody().getFranchiseName());
                })

                .verifyComplete();
    }

    @Test
    void testDeleteFranchisieByIdController() {

        String franchiseId = "1";

        when(franchiseService.deleteFranchiseById(franchiseId)).thenReturn(Mono.empty());

        Mono<ResponseEntity<Void>> response = franchiseController.deleteFranchiseById(franchiseId);

        StepVerifier.create(response)
                .consumeNextWith(entity -> {
                    assertEquals(HttpStatus.NO_CONTENT, entity.getStatusCode());

                    assertNull(entity.getBody());
                })

                .verifyComplete();

        verify(franchiseService, times(1)).deleteFranchiseById(franchiseId);
    }
}
