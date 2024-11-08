package com.application.prueba.controllers;

import com.application.prueba.dtos.NewNameDTO;
import com.application.prueba.dtos.ProductDTO;
import com.application.prueba.dtos.StoreDTO;
import com.application.prueba.entities.Product;
import com.application.prueba.entities.Store;
import com.application.prueba.mappers.StoreMapper;
import com.application.prueba.services.StoreService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class StoreControllerTest {


    @Mock
    private StoreService storeService;

    @Mock
    private StoreMapper storeMapper;

    @InjectMocks
    private StoreController storeController;

    @Test
    void testGetStoreById() {

        String storeId = "1";

        Product product = Product.builder()
                .id("p1")
                .productName("Product 1")
                .stock(100)
                .build();

        ProductDTO productDTO = ProductDTO.builder()
                .idDTO("p1")
                .productNameDTO("Product 1")
                .stockDTO(100)
                .build();

        Store store = Store.builder()
                .id("1")
                .storeName("Store Example")
                .productList(Collections.singletonList(product))
                .build();

        StoreDTO storeDTO = StoreDTO.builder()
                .idDTO("1")
                .storeNameDTO("Store Example")
                .productDTOList(Collections.singletonList(productDTO))
                .build();

        when(storeService.findStoreById(storeId)).thenReturn(Mono.just(store));
        when(storeMapper.storeToStoreDTO(store)).thenReturn(storeDTO);

        Mono<ResponseEntity<StoreDTO>> response = storeController.getStoreById(storeId);

        StepVerifier.create(response)
                .consumeNextWith(entity -> {
                    assertEquals(HttpStatus.OK, entity.getStatusCode());
                    assertNotNull(entity.getBody());
                    assertEquals("1", entity.getBody().getIdDTO());
                    assertEquals("Store Example", entity.getBody().getStoreNameDTO());
                    assertEquals(1, entity.getBody().getProductDTOList().size());
                    assertEquals("Product 1", entity.getBody().getProductDTOList().get(0).getProductNameDTO());
                })

                .verifyComplete();

        verify(storeService, times(1)).findStoreById(storeId);
        verify(storeMapper, times(1)).storeToStoreDTO(store);
    }

    @Test
    void testGetAllStores() {

        Product product1 = Product.builder()
                .id("p1")
                .productName("Product 1")
                .stock(100)
                .build();

        Product product2 = Product.builder()
                .id("p2")
                .productName("Product 2")
                .stock(200)
                .build();

        ProductDTO productDTO1 = ProductDTO.builder()
                .idDTO("p1")
                .productNameDTO("Product 1")
                .stockDTO(100)
                .build();

        ProductDTO productDTO2 = ProductDTO.builder()
                .idDTO("p2")
                .productNameDTO("Product 2")
                .stockDTO(200)
                .build();

        Store store1 = Store.builder()
                .id("1")
                .storeName("Store Example 1")
                .productList(Collections.singletonList(product1))
                .build();

        Store store2 = Store.builder()
                .id("2")
                .storeName("Store Example 2")
                .productList(Collections.singletonList(product2))
                .build();

        StoreDTO storeDTO1 = StoreDTO.builder()
                .idDTO("1")
                .storeNameDTO("Store Example 1")
                .productDTOList(Collections.singletonList(productDTO1))
                .build();

        StoreDTO storeDTO2 = StoreDTO.builder()
                .idDTO("2")
                .storeNameDTO("Store Example 2")
                .productDTOList(Collections.singletonList(productDTO2))
                .build();

        List<Store> stores = Arrays.asList(store1, store2);

        when(storeService.findAllStores()).thenReturn(Flux.fromIterable(stores));
        when(storeMapper.storeToStoreDTO(store1)).thenReturn(storeDTO1);
        when(storeMapper.storeToStoreDTO(store2)).thenReturn(storeDTO2);

        Flux<ResponseEntity<StoreDTO>> response = storeController.getAllStores();

        StepVerifier.create(response)
                .consumeNextWith(entity -> {
                    assertEquals(HttpStatus.OK, entity.getStatusCode());
                    assertNotNull(entity.getBody());
                    assertEquals("Store Example 1", entity.getBody().getStoreNameDTO());
                })

                .consumeNextWith(entity -> {
                    assertEquals(HttpStatus.OK, entity.getStatusCode());
                    assertNotNull(entity.getBody());
                    assertEquals("Store Example 2", entity.getBody().getStoreNameDTO());
                })

                .verifyComplete();

        verify(storeService, times(1)).findAllStores();
        verify(storeMapper, times(1)).storeToStoreDTO(store1);
        verify(storeMapper, times(1)).storeToStoreDTO(store2);
    }

    @Test
    void testCreateStore() {

        ProductDTO productDTO = ProductDTO.builder()
                .idDTO("p1")
                .productNameDTO("Product 1")
                .stockDTO(100)
                .build();

        List<ProductDTO> productDTOList = Collections.singletonList(productDTO);

        StoreDTO storeDTO = StoreDTO.builder()
                .idDTO("1")
                .storeNameDTO("Store Example")
                .productDTOList(productDTOList)
                .build();

        Store store = Store.builder()
                .id("1")
                .storeName("Store Example")
                .productList(Collections.singletonList(
                        Product.builder()
                                .id("p1")
                                .productName("Product 1")
                                .stock(100)
                                .build()))
                .build();

        StoreDTO savedStoreDTO = StoreDTO.builder()
                .idDTO("1")
                .storeNameDTO("Store Example")
                .productDTOList(productDTOList)
                .build();

        when(storeMapper.storeDTOToStore(storeDTO)).thenReturn(store);
        when(storeMapper.storeToStoreDTO(store)).thenReturn(savedStoreDTO);
        when(storeService.saveStore(storeDTO)).thenReturn(Mono.just(savedStoreDTO));

        Mono<ResponseEntity<StoreDTO>> response = storeController.createStore(storeDTO);

        StepVerifier.create(response)
                .consumeNextWith(entity -> {
                    assertEquals(HttpStatus.CREATED, entity.getStatusCode());
                    assertNotNull(entity.getBody());
                    assertEquals("Store Example", entity.getBody().getStoreNameDTO());
                    assertEquals("1", entity.getBody().getIdDTO());
                })

                .verifyComplete();

        verify(storeService, times(1)).saveStore(storeDTO);
        verify(storeMapper, times(1)).storeDTOToStore(storeDTO);
        verify(storeMapper, times(1)).storeToStoreDTO(store);
    }

    @Test
    void testAddProductToStore() {

        ProductDTO productDTO = ProductDTO.builder()
                .idDTO("p1")
                .productNameDTO("Product 1")
                .stockDTO(100)
                .build();

        Store store = Store.builder()
                .id("1")
                .storeName("Store Example")
                .productList(Collections.emptyList())
                .build();

        StoreDTO savedStoreDTO = StoreDTO.builder()
                .idDTO("1")
                .storeNameDTO("Store Example")
                .productDTOList(Collections.singletonList(productDTO))
                .build();

        when(storeService.saveProductToStore("1", productDTO)).thenReturn(Mono.just(store));
        when(storeMapper.storeToStoreDTO(store)).thenReturn(savedStoreDTO);

        Mono<ResponseEntity<StoreDTO>> response = storeController.addProductToStore("1", productDTO);

        StepVerifier.create(response)
                .consumeNextWith(entity -> {
                    assertEquals(HttpStatus.OK, entity.getStatusCode());
                    assertNotNull(entity.getBody());
                    assertEquals("Store Example", entity.getBody().getStoreNameDTO());
                    assertEquals("1", entity.getBody().getIdDTO());
                    assertEquals(1, entity.getBody().getProductDTOList().size());
                    assertEquals("Product 1", entity.getBody().getProductDTOList().get(0).getProductNameDTO());
                })

                .verifyComplete();

        verify(storeService, times(1)).saveProductToStore("1", productDTO);
        verify(storeMapper, times(1)).storeToStoreDTO(store);
    }

    @Test
    void testDeleteProductFromStore() {

        String storeId = "1";
        String productId = "p1";

        Store updatedStore = Store.builder()
                .id("1")
                .storeName("Store Example")
                .productList(Collections.emptyList())
                .build();

        StoreDTO updatedStoreDTO = StoreDTO.builder()
                .idDTO("1")
                .storeNameDTO("Store Example")
                .productDTOList(Collections.emptyList())
                .build();

        when(storeService.deleteProductFromStore(storeId, productId)).thenReturn(Mono.just(updatedStore));

        when(storeMapper.storeToStoreDTO(updatedStore)).thenReturn(updatedStoreDTO);

        Mono<ResponseEntity<StoreDTO>> response = storeController.deleteProductFromStore(storeId, productId);

        StepVerifier.create(response)
                .consumeNextWith(entity -> {
                    assertEquals(HttpStatus.OK, entity.getStatusCode());
                    assertNotNull(entity.getBody());
                    assertEquals("Store Example", entity.getBody().getStoreNameDTO());
                    assertEquals("1", entity.getBody().getIdDTO());
                    assertTrue(entity.getBody().getProductDTOList().isEmpty());  // Verificar que el producto fue eliminado
                })

                .verifyComplete();

        verify(storeService, times(1)).deleteProductFromStore(storeId, productId);

        verify(storeMapper, times(1)).storeToStoreDTO(updatedStore);
    }

    @Test
    void testUpdateStoreName() {

        String storeId = "1";

        String newName = "Updated Store Name";

        NewNameDTO newNameDTO = NewNameDTO.builder()
                .newNameDTO(newName)
                .build();

        Store storeAfterUpdate = Store.builder()
                .id("1")
                .storeName(newName)
                .productList(Collections.emptyList())
                .build();

        StoreDTO updatedStoreDTO = StoreDTO.builder()
                .idDTO("1")
                .storeNameDTO(newName)
                .productDTOList(Collections.emptyList())
                .build();

        when(storeService.updateStoreName(storeId, newNameDTO)).thenReturn(Mono.just(storeAfterUpdate));

        when(storeMapper.storeToStoreDTO(storeAfterUpdate)).thenReturn(updatedStoreDTO);

        Mono<ResponseEntity<StoreDTO>> response = storeController.updateStoreName(storeId, newNameDTO);

        StepVerifier.create(response)
                .consumeNextWith(entity -> {
                    assertEquals(HttpStatus.OK, entity.getStatusCode());
                    assertNotNull(entity.getBody());
                    assertEquals(newName, entity.getBody().getStoreNameDTO());
                    assertEquals(storeId, entity.getBody().getIdDTO());
                })

                .verifyComplete();

        verify(storeService, times(1)).updateStoreName(storeId, newNameDTO);
        verify(storeMapper, times(1)).storeToStoreDTO(storeAfterUpdate);
    }

    @Test
    void testDeleteProductById() {

        String storeId = "1";

        when(storeService.deleteStoreById(storeId)).thenReturn(Mono.empty());

        Mono<ResponseEntity<Void>> response = storeController.deleteStoreById(storeId);

        StepVerifier.create(response)
                .consumeNextWith(entity -> assertEquals(HttpStatus.NO_CONTENT, entity.getStatusCode()))
                .verifyComplete();

        verify(storeService, times(1)).deleteStoreById(storeId);
    }
}
