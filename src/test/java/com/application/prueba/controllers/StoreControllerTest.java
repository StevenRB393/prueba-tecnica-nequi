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
import reactor.core.publisher.Mono;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StoreControllerTest {


    @Mock
    private StoreService storeService;  // Mock del servicio

    @Mock
    private StoreMapper storeMapper;    // Mock del mapper

    @InjectMocks
    private StoreController storeController;  // Controlador que se va a probar



    @Test
    void testGetStoreById() {
        String storeId = "1";

        // Crear productos para la tienda (Product es una entidad, ProductDTO es el DTO)
        Product product = new Product("p1", "Product 1", 100);
        ProductDTO productDTO = new ProductDTO("p1", "Product 1", 100);

        // Crear el objeto Store con una lista de productos (en entidad Store)
        Store store = new Store();
        store.setId("1");
        store.setStoreName("Store Example");
        store.setProductList(Collections.singletonList(product));  // Lista de productos

        // Crear el StoreDTO con los productos mapeados
        StoreDTO storeDTO = new StoreDTO("1", "Store Example", Collections.singletonList(productDTO)); // Lista de ProductDTO

        // Mockear el comportamiento del servicio
        when(storeService.findStoreById(storeId)).thenReturn(Mono.just(store));

        // Mockear el comportamiento del mapper
        when(storeMapper.storeToStoreDTO(store)).thenReturn(storeDTO);

        // Ejecutar el controlador
        Mono<ResponseEntity<StoreDTO>> response = storeController.getStoreById(storeId);

        // Verificar con StepVerifier
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

        // Verificar que el método del servicio fue llamado con el ID correcto
        verify(storeService, times(1)).findStoreById(storeId);

        // Verificar que el mapper fue llamado
        verify(storeMapper, times(1)).storeToStoreDTO(store);
    }

    @Test
    void testGetAllStores() {

        Product product1 = new Product("p1", "Product 1", 100);
        Product product2 = new Product("p2", "Product 2", 200);

        ProductDTO productDTO1 = new ProductDTO("p1", "Product 1", 100);
        ProductDTO productDTO2 = new ProductDTO("p2", "Product 2", 200);

        // Crear las tiendas (Store)
        Store store1 = new Store("1", "Store Example 1", Collections.singletonList(product1));
        Store store2 = new Store("2", "Store Example 2", Collections.singletonList(product2));

        // Crear las StoreDTO
        StoreDTO storeDTO1 = new StoreDTO("1", "Store Example 1", Collections.singletonList(productDTO1));
        StoreDTO storeDTO2 = new StoreDTO("2", "Store Example 2", Collections.singletonList(productDTO2));

        // Crear una lista de tiendas (Store)
        List<Store> stores = Arrays.asList(store1, store2);

        // Crear una lista de StoreDTO
        List<StoreDTO> storeDTOs = Arrays.asList(storeDTO1, storeDTO2);

        // Mockear el comportamiento del servicio
        when(storeService.findAllStores()).thenReturn(Flux.fromIterable(stores));

        // Mockear el comportamiento del mapper
        when(storeMapper.storeToStoreDTO(store1)).thenReturn(storeDTO1);
        when(storeMapper.storeToStoreDTO(store2)).thenReturn(storeDTO2);

        // Ejecutar el controlador y obtener la respuesta como Flux<ResponseEntity<StoreDTO>>
        Flux<ResponseEntity<StoreDTO>> response = storeController.getAllStores();

        // Verificar con StepVerifier
        StepVerifier.create(response)
                .consumeNextWith(entity -> {
                    // Verificar que el código de estado y que el cuerpo no es null para la primera tienda
                    assertEquals(HttpStatus.OK, entity.getStatusCode());
                    assertNotNull(entity.getBody());
                    assertEquals("Store Example 1", entity.getBody().getStoreNameDTO());
                })
                .consumeNextWith(entity -> {
                    // Verificar que el código de estado y que el cuerpo no es null para la segunda tienda
                    assertEquals(HttpStatus.OK, entity.getStatusCode());
                    assertNotNull(entity.getBody());
                    assertEquals("Store Example 2", entity.getBody().getStoreNameDTO());
                })
                .verifyComplete();

        // Verificar que el método del servicio fue llamado
        verify(storeService, times(1)).findAllStores();

        // Verificar que el mapper fue llamado para las tiendas
        verify(storeMapper, times(1)).storeToStoreDTO(store1);
        verify(storeMapper, times(1)).storeToStoreDTO(store2);


    }

    @Test
    void testCreateStore() {
        ProductDTO productDTO = new ProductDTO("p1", "Product 1", 100);
        List<ProductDTO> productDTOList = Collections.singletonList(productDTO);
        StoreDTO storeDTO = new StoreDTO("1", "Store Example", productDTOList);

        // Crear el Store de salida (lo que se guardará)
        Store store = new Store("1", "Store Example", Collections.singletonList(new Product("p1", "Product 1", 100)));

        // Crear el StoreDTO que devolveremos tras guardar
        StoreDTO savedStoreDTO = new StoreDTO("1", "Store Example", productDTOList);

        // Mockear el comportamiento del mapper
        when(storeMapper.storeDTOToStore(storeDTO)).thenReturn(store);
        when(storeMapper.storeToStoreDTO(store)).thenReturn(savedStoreDTO);

        // Mockear el comportamiento del servicio
        when(storeService.saveStore(storeDTO)).thenReturn(Mono.just(savedStoreDTO));

        // Ejecutar el controlador para obtener la respuesta
        Mono<ResponseEntity<StoreDTO>> response = storeController.createStore(storeDTO);

        // Verificar que la respuesta es la esperada (HttpStatus.CREATED y el cuerpo correcto)
        StepVerifier.create(response)
                .consumeNextWith(entity -> {
                    assertEquals(HttpStatus.CREATED, entity.getStatusCode());
                    assertNotNull(entity.getBody());
                    assertEquals("Store Example", entity.getBody().getStoreNameDTO());
                    assertEquals("1", entity.getBody().getIdDTO());
                })
                .verifyComplete();

        // Verificar que el método del servicio fue llamado
        verify(storeService, times(1)).saveStore(storeDTO);

        // Verificar que el mapper fue llamado
        verify(storeMapper, times(1)).storeDTOToStore(storeDTO);
        verify(storeMapper, times(1)).storeToStoreDTO(store);
    }

    @Test
    void testAddProductToStore() {
        ProductDTO productDTO = new ProductDTO("p1", "Product 1", 100);

        // Crear el Store (inicialmente vacío de productos)
        Store store = new Store("1", "Store Example", Collections.emptyList());

        // Crear el StoreDTO que devolveremos tras agregar el producto
        StoreDTO savedStoreDTO = new StoreDTO("1", "Store Example", Collections.singletonList(productDTO));

        // Mockear el comportamiento del servicio
        when(storeService.saveProductToStore("1", productDTO)).thenReturn(Mono.just(store));

        // Mockear el comportamiento del mapper
        when(storeMapper.storeToStoreDTO(store)).thenReturn(savedStoreDTO);

        // Ejecutar el controlador para agregar el producto a la tienda
        Mono<ResponseEntity<StoreDTO>> response = storeController.addProductToStore("1", productDTO);

        // Verificar que la respuesta es la esperada (HttpStatus.OK y el cuerpo correcto)
        StepVerifier.create(response)
                .consumeNextWith(entity -> {
                    assertEquals(HttpStatus.OK, entity.getStatusCode());
                    assertNotNull(entity.getBody());
                    assertEquals("Store Example", entity.getBody().getStoreNameDTO());
                    assertEquals("1", entity.getBody().getIdDTO());
                    assertEquals(1, entity.getBody().getProductDTOList().size());  // Verificar que el producto fue agregado
                    assertEquals("Product 1", entity.getBody().getProductDTOList().get(0).getProductNameDTO());
                })
                .verifyComplete();

        // Verificar que el método del servicio fue llamado con los parámetros correctos
        verify(storeService, times(1)).saveProductToStore("1", productDTO);

        // Verificar que el mapper fue llamado para convertir Store a StoreDTO
        verify(storeMapper, times(1)).storeToStoreDTO(store); // Solo verificamos esto ya que el producto se agrega en el servicio
    }

    @Test
    void testDeleteProductFromStore() {
        String storeId = "1";
        String productId = "p1";

        // Crear un StoreDTO que será retornado después de eliminar el producto
        ProductDTO productDTO = new ProductDTO("p1", "Product 1", 100);
        StoreDTO storeDTO = new StoreDTO("1", "Store Example", Collections.singletonList(productDTO));

        // Eliminar el producto de la tienda (simulando el comportamiento del servicio)
        Store updatedStore = new Store("1", "Store Example", Collections.emptyList());

        // Crear el StoreDTO actualizado
        StoreDTO updatedStoreDTO = new StoreDTO("1", "Store Example", Collections.emptyList());

        // Mockear el comportamiento del servicio
        when(storeService.deleteProductFromStore(storeId, productId)).thenReturn(Mono.just(updatedStore));

        // Mockear el comportamiento del mapper
        when(storeMapper.storeToStoreDTO(updatedStore)).thenReturn(updatedStoreDTO);

        // Ejecutar el controlador para eliminar el producto
        Mono<ResponseEntity<StoreDTO>> response = storeController.deleteProductFromStore(storeId, productId);

        // Verificar que la respuesta es la esperada (HttpStatus.OK y el cuerpo correcto)
        StepVerifier.create(response)
                .consumeNextWith(entity -> {
                    assertEquals(HttpStatus.OK, entity.getStatusCode());
                    assertNotNull(entity.getBody());
                    assertEquals("Store Example", entity.getBody().getStoreNameDTO());
                    assertEquals("1", entity.getBody().getIdDTO());
                    assertTrue(entity.getBody().getProductDTOList().isEmpty());  // Verificar que el producto fue eliminado
                })
                .verifyComplete();

        // Verificar que el método del servicio fue llamado con los parámetros correctos
        verify(storeService, times(1)).deleteProductFromStore(storeId, productId);

        // Verificar que el mapper fue llamado para convertir Store a StoreDTO
        verify(storeMapper, times(1)).storeToStoreDTO(updatedStore);
    }

    @Test
    void testUpdateStoreName() {
        String storeId = "1";
        String newName = "Updated Store Name";
        NewNameDTO newNameDTO = new NewNameDTO();
        newNameDTO.setNewNameDTO(newName);  // Asumimos que este campo en NewNameDTO es "storeNameDTO"

        // Crear un Store con el nombre actualizado
        Store storeBeforeUpdate = new Store("1", "Old Store Name", Collections.emptyList());
        Store storeAfterUpdate = new Store("1", newName, Collections.emptyList());

        // Crear el StoreDTO correspondiente después de la actualización
        StoreDTO updatedStoreDTO = new StoreDTO("1", newName, Collections.emptyList());

        // Mockear el comportamiento del servicio para actualizar el nombre de la tienda
        when(storeService.updateStoreName(storeId, newNameDTO)).thenReturn(Mono.just(storeAfterUpdate));

        // Mockear el comportamiento del mapper
        when(storeMapper.storeToStoreDTO(storeAfterUpdate)).thenReturn(updatedStoreDTO);

        // Ejecutar el controlador para actualizar el nombre de la tienda
        Mono<ResponseEntity<StoreDTO>> response = storeController.updateStoreName(storeId, newNameDTO);

        // Verificar que la respuesta es la esperada (HttpStatus.OK y el cuerpo correcto)
        StepVerifier.create(response)
                .consumeNextWith(entity -> {
                    assertEquals(HttpStatus.OK, entity.getStatusCode());
                    assertNotNull(entity.getBody());
                    assertEquals(newName, entity.getBody().getStoreNameDTO());
                    assertEquals(storeId, entity.getBody().getIdDTO());
                })
                .verifyComplete();

        // Verificar que el método del servicio fue llamado con los parámetros correctos
        verify(storeService, times(1)).updateStoreName(storeId, newNameDTO);

        // Verificar que el mapper fue llamado para convertir Store a StoreDTO
        verify(storeMapper, times(1)).storeToStoreDTO(storeAfterUpdate);
    }

    @Test
    void testDeleteProductById() {
        String storeId = "1";

        // Mockear el comportamiento del servicio para una eliminación exitosa
        when(storeService.deleteStoreById(storeId)).thenReturn(Mono.empty());

        // Llamar al método del controlador
        Mono<ResponseEntity<Void>> response = storeController.deleteStoreById(storeId);

        // Verificar que la respuesta es la esperada (HttpStatus.NO_CONTENT)
        StepVerifier.create(response)
                .consumeNextWith(entity -> assertEquals(HttpStatus.NO_CONTENT, entity.getStatusCode()))
                .verifyComplete();

        // Verificar que el método del servicio fue llamado una vez con el ID correcto
        verify(storeService, times(1)).deleteStoreById(storeId);
    }

}
