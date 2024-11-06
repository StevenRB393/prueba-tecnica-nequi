package com.application.prueba.controllers;

import com.application.prueba.dtos.NewNameDTO;
import com.application.prueba.dtos.NewStockDTO;
import com.application.prueba.dtos.ProductDTO;
import com.application.prueba.entities.Product;
import com.application.prueba.mappers.ProductMapper;
import com.application.prueba.services.ProductService;
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
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.WARN)
public class ProductControllerTest {

    @InjectMocks
    private ProductController productController;

    @Mock
    private ProductService productService;

    @Mock
    private ProductMapper productMapper;

    @Test
    void getProductByIdController() {

        String productId = "1";
        Product product = new Product(productId, "Sample Product", 10);
        ProductDTO productDTO = new ProductDTO(productId, "Sample Product", 10);

        // Mockear el comportamiento
        when(productService.findProductById(productId)).thenReturn(Mono.just(product));
        when(productMapper.productToProductDTO(product)).thenReturn(productDTO);

        // Ejecutar el controlador
        Mono<ResponseEntity<ProductDTO>> response = productController.getProductById(productId);

        // Verificar con StepVerifier
        StepVerifier.create(response)
                .consumeNextWith(entity -> {
                    assertEquals(HttpStatus.OK, entity.getStatusCode());
                    assertNotNull(entity.getBody());
                    assertEquals("Sample Product", entity.getBody().getProductNameDTO());
                    assertEquals(10, entity.getBody().getStockDTO());
                })
                .verifyComplete();

        // Verificar las invocaciones de los mocks
        verify(productService).findProductById(productId);
        verify(productMapper).productToProductDTO(product);
    }

    @Test
    void getAllProductController() {
        Product product1 = new Product("1", "Product 1", 10);
        Product product2 = new Product("2", "Product 2", 20);
        ProductDTO productDTO1 = new ProductDTO("1", "Product 1", 10);
        ProductDTO productDTO2 = new ProductDTO("2", "Product 2", 20);

        // Mockear el comportamiento
        when(productService.findAllProducts()).thenReturn(Flux.just(product1, product2));
        when(productMapper.productToProductDTO(product1)).thenReturn(productDTO1);
        when(productMapper.productToProductDTO(product2)).thenReturn(productDTO2);

        // Ejecutar el controlador
        Flux<ResponseEntity<ProductDTO>> response = productController.getAllProducts();

        // Verificar con StepVerifier
        StepVerifier.create(response)
                .consumeNextWith(entity -> {
                    assertEquals(HttpStatus.OK, entity.getStatusCode());
                    assertNotNull(entity.getBody());
                    assertEquals("Product 1", entity.getBody().getProductNameDTO());
                    assertEquals(10, entity.getBody().getStockDTO());
                })
                .consumeNextWith(entity -> {
                    assertEquals(HttpStatus.OK, entity.getStatusCode());
                    assertNotNull(entity.getBody());
                    assertEquals("Product 2", entity.getBody().getProductNameDTO());
                    assertEquals(20, entity.getBody().getStockDTO());
                })
                .verifyComplete();

        // Verificar las invocaciones de los mocks
        verify(productService).findAllProducts();
        verify(productMapper).productToProductDTO(product1);
        verify(productMapper).productToProductDTO(product2);
    }

    @Test
    void saveProductController() {
        Product product1 = new Product("1", "Product 1", 10);
        Product product2 = new Product("2", "Product 2", 20);
        ProductDTO productDTO1 = new ProductDTO("1", "Product 1", 10);
        ProductDTO productDTO2 = new ProductDTO("2", "Product 2", 20);

        when(productService.findAllProducts()).thenReturn(Flux.just(product1, product2));
        when(productMapper.productToProductDTO(any(Product.class))).thenReturn(productDTO1, productDTO2);

        // Act
        Flux<ResponseEntity<ProductDTO>> response = productController.getAllProducts();

        // Assert
        StepVerifier.create(response)
                .expectNext(ResponseEntity.ok(productDTO1))
                .expectNext(ResponseEntity.ok(productDTO2))
                .verifyComplete();

        verify(productService, times(1)).findAllProducts();  // Verificar que se haya llamado al servicio
    }

    @Test
    void updateStockController() {
        String productId = "1";
        NewStockDTO newStockDTO = new NewStockDTO(15);  // Asumimos que se quiere actualizar a 15 unidades en stock
        Product product = new Product("1", "Product 1", 10);
        Product updatedProduct = new Product("1", "Product 1", 15);  // Producto después de la actualización
        ProductDTO productDTO = new ProductDTO("1", "Product 1", 15);

        // Simulamos el comportamiento del servicio
        when(productService.modifyProductStock(eq(productId), eq(newStockDTO))).thenReturn(Mono.just(updatedProduct));
        when(productMapper.productToProductDTO(updatedProduct)).thenReturn(productDTO);

        // Act
        Mono<ResponseEntity<ProductDTO>> response = productController.updateStock(productId, newStockDTO);

        // Assert
        StepVerifier.create(response)
                .expectNext(ResponseEntity.ok(productDTO))
                .verifyComplete();

        verify(productService, times(1)).modifyProductStock(eq(productId), eq(newStockDTO));  // Verificar llamada al servicio
    }

    @Test
    void updateProductNameController() {
        String productId = "1";
        NewNameDTO newNameDTO = new NewNameDTO("Updated Product Name");
        Product product = new Product("1", "Old Product Name", 10);
        Product updatedProduct = new Product("1", "Updated Product Name", 10);
        ProductDTO productDTO = new ProductDTO("1", "Updated Product Name", 10);

        // Simulamos el comportamiento del servicio
        when(productService.updateProductName(eq(productId), eq(newNameDTO))).thenReturn(Mono.just(updatedProduct));
        when(productMapper.productToProductDTO(updatedProduct)).thenReturn(productDTO);

        // Act
        Mono<ResponseEntity<ProductDTO>> response = productController.updateProductName(productId, newNameDTO);

        // Assert
        StepVerifier.create(response)
                .expectNext(ResponseEntity.ok(productDTO))  // Verificar que la respuesta es "ok" con el DTO actualizado
                .verifyComplete();

        // Verificamos que el servicio fue llamado exactamente una vez
        verify(productService, times(1)).updateProductName(eq(productId), eq(newNameDTO));
    }

    @Test
    void deleteProductByIdController() {
        String productId = "1";

        // Simulamos el comportamiento del servicio
        when(productService.deleteProductById(eq(productId))).thenReturn(Mono.empty());  // El producto se elimina exitosamente

        // Act
        Mono<ResponseEntity<Void>> response = productController.deleteProduct(productId);

        // Assert
        StepVerifier.create(response)
                .expectNext(ResponseEntity.noContent().build())  // Verificar que la respuesta es "no content" (204)
                .verifyComplete();

        // Verificamos que el servicio fue llamado exactamente una vez
        verify(productService, times(1)).deleteProductById(eq(productId));

    }

}


