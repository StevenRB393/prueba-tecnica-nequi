package com.application.prueba.controllers;

import com.application.prueba.dtos.NewNameDTO;
import com.application.prueba.dtos.NewStockDTO;
import com.application.prueba.dtos.ProductDTO;
import com.application.prueba.models.Product;
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

        Product product = Product.builder()
                .id(productId)
                .productName("Sample Product")
                .stock(10)
                .build();

        ProductDTO productDTO = ProductDTO.builder()
                .idDTO(productId)
                .productNameDTO("Sample Product")
                .stockDTO(10)
                .build();


        when(productService.findProductById(productId)).thenReturn(Mono.just(product));
        when(productMapper.productToProductDTO(product)).thenReturn(productDTO);

        Mono<ResponseEntity<ProductDTO>> response = productController.getProductById(productId);

        StepVerifier.create(response)
                .consumeNextWith(entity -> {
                    assertEquals(HttpStatus.OK, entity.getStatusCode());
                    assertNotNull(entity.getBody());
                    assertEquals("Sample Product", entity.getBody().getProductNameDTO());
                    assertEquals(10, entity.getBody().getStockDTO());
                })

                .verifyComplete();

        verify(productService).findProductById(productId);
        verify(productMapper).productToProductDTO(product);
    }

    @Test
    void getAllProductController() {

        Product product1 = Product.builder()
                .id("1")
                .productName("Product 1")
                .stock(10)
                .build();

        Product product2 = Product.builder()
                .id("2")
                .productName("Product 2")
                .stock(20)
                .build();

        ProductDTO productDTO1 = ProductDTO.builder()
                .idDTO("1")
                .productNameDTO("Product 1")
                .stockDTO(10)
                .build();

        ProductDTO productDTO2 = ProductDTO.builder()
                .idDTO("2")
                .productNameDTO("Product 2")
                .stockDTO(20)
                .build();

        when(productService.findAllProducts()).thenReturn(Flux.just(product1, product2));
        when(productMapper.productToProductDTO(product1)).thenReturn(productDTO1);
        when(productMapper.productToProductDTO(product2)).thenReturn(productDTO2);

        Flux<ResponseEntity<ProductDTO>> response = productController.getAllProducts();

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

        verify(productService).findAllProducts();
        verify(productMapper).productToProductDTO(product1);
        verify(productMapper).productToProductDTO(product2);
    }

    @Test
    void saveProductController() {

        Product product1 = Product.builder()
                .id("1")
                .productName("Product 1")
                .stock(10)
                .build();

        Product product2 = Product.builder()
                .id("2")
                .productName("Product 2")
                .stock(20)
                .build();

        ProductDTO productDTO1 = ProductDTO.builder()
                .idDTO("1")
                .productNameDTO("Product 1")
                .stockDTO(10)
                .build();

        ProductDTO productDTO2 = ProductDTO.builder()
                .idDTO("2")
                .productNameDTO("Product 2")
                .stockDTO(20)
                .build();

        when(productService.findAllProducts()).thenReturn(Flux.just(product1, product2));
        when(productMapper.productToProductDTO(any(Product.class))).thenReturn(productDTO1, productDTO2);

        Flux<ResponseEntity<ProductDTO>> response = productController.getAllProducts();

        StepVerifier.create(response)
                .expectNext(ResponseEntity.ok(productDTO1))
                .expectNext(ResponseEntity.ok(productDTO2))
                .verifyComplete();

        verify(productService, times(1)).findAllProducts();
    }

    @Test
    void updateStockController() {

        String productId = "1";

        NewStockDTO newStockDTO = NewStockDTO.builder()
                .newStockDTO(15)
                .build();

        Product updatedProduct = Product.builder()
                .id("1")
                .productName("Product 1")
                .stock(15)
                .build();

        ProductDTO productDTO = ProductDTO.builder()
                .idDTO("1")
                .productNameDTO("Product 1")
                .stockDTO(15)
                .build();

        when(productService.modifyProductStock(productId, newStockDTO)).thenReturn(Mono.just(updatedProduct));
        when(productMapper.productToProductDTO(updatedProduct)).thenReturn(productDTO);

        Mono<ResponseEntity<ProductDTO>> response = productController.updateStock(productId, newStockDTO);

        StepVerifier.create(response)
                .expectNext(ResponseEntity.ok(productDTO))
                .verifyComplete();

        verify(productService, times(1)).modifyProductStock(productId, newStockDTO);
    }

    @Test
    void updateProductNameController() {

        String productId = "1";

        NewNameDTO newNameDTO = NewNameDTO.builder()
                .newNameDTO("Updated Product Name")
                .build();

        Product updatedProduct = Product.builder()
                .id("1")
                .productName("Updated Product Name")
                .stock(10)
                .build();

        ProductDTO productDTO = ProductDTO.builder()
                .idDTO("1")
                .productNameDTO("Updated Product Name")
                .stockDTO(10)
                .build();

        when(productService.updateProductName(productId, newNameDTO)).thenReturn(Mono.just(updatedProduct));
        when(productMapper.productToProductDTO(updatedProduct)).thenReturn(productDTO);

        Mono<ResponseEntity<ProductDTO>> response = productController.updateProductName(productId, newNameDTO);

        StepVerifier.create(response)
                .expectNext(ResponseEntity.ok(productDTO))
                .verifyComplete();

        verify(productService, times(1)).updateProductName(productId, newNameDTO);

    }

    @Test
    void deleteProductByIdController() {

        String productId = "1";

        when(productService.deleteProductById(productId)).thenReturn(Mono.empty());

        Mono<ResponseEntity<Void>> response = productController.deleteProduct(productId);

        StepVerifier.create(response)
                .expectNext(ResponseEntity.noContent().build())
                .verifyComplete();

        verify(productService, times(1)).deleteProductById(productId);
    }
}


