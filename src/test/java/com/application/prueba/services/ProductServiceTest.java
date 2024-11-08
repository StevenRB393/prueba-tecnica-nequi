package com.application.prueba.services;

import static org.mockito.Mockito.*;
import com.application.prueba.dtos.NewNameDTO;
import com.application.prueba.dtos.NewStockDTO;
import com.application.prueba.dtos.ProductDTO;
import com.application.prueba.models.Product;
import com.application.prueba.repositories.ProductRepository;
import com.application.prueba.services.impl.ProductServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @InjectMocks
    private ProductServiceImpl productService;

    @Mock
    private ProductRepository productRepository;

    @Test
    void testFindProductById() {

        String productId = "product1";
        Product product = Product.builder()
                .id(productId)
                .productName("Product One")
                .stock(10)
                .build();

        when(productRepository.findById(productId)).thenReturn(Mono.just(product));

        Mono<Product> result = productService.findProductById(productId);

        StepVerifier.create(result)
                .expectNextMatches(p -> p.getId().equals(productId) && p.getProductName().equals("Product One"))

                .verifyComplete();
    }

    @Test
    void testFindAllProducts() {

        Product product1 = Product.builder()
                .id("product1")
                .productName("Product One")
                .stock(10)
                .build();

        Product product2 = Product.builder()
                .id("product2")
                .productName("Product Two")
                .stock(20)
                .build();

        when(productRepository.findAll()).thenReturn(Flux.just(product1, product2));

        Flux<Product> result = productService.findAllProducts();

        StepVerifier.create(result)
                .expectNext(product1)
                .expectNext(product2)

                .verifyComplete();
    }

    @Test
    void testSaveProduct() {

        ProductDTO productDTO = ProductDTO.builder()
                .idDTO("product1")
                .productNameDTO("Product One")
                .stockDTO(10)
                .build();

        Product product = Product.builder()
                .id(productDTO.getIdDTO())
                .productName(productDTO.getProductNameDTO())
                .stock(productDTO.getStockDTO())
                .build();

        when(productRepository.save(any(Product.class))).thenReturn(Mono.just(product));

        Mono<ProductDTO> result = productService.saveProduct(productDTO);

        StepVerifier.create(result)
                .expectNextMatches(p -> p.getIdDTO().equals("product1") && p.getProductNameDTO().equals("Product One"))

                .verifyComplete();
    }

    @Test
    void testModifyProductStock() {

        String productId = "product1";

        NewStockDTO newStockDTO = NewStockDTO.builder()
                .newStockDTO(15)
                .build();

        Product existingProduct = Product.builder()
                .id(productId)
                .productName("Product Name")
                .stock(10)
                .build();

        when(productRepository.findById(productId)).thenReturn(Mono.just(existingProduct));
        when(productRepository.save(any(Product.class))).thenReturn(Mono.just(new Product(productId, "Product Name", newStockDTO.getNewStockDTO())));

        Mono<Product> result = productService.modifyProductStock(productId, newStockDTO);

        StepVerifier.create(result)
                .expectNextMatches(p -> p.getStock() == newStockDTO.getNewStockDTO())

                .verifyComplete();
    }

    @Test
    void testUpdateProductName() {

        String productId = "product1";
        NewNameDTO newNameDTO = NewNameDTO.builder()
                .newNameDTO("New Product Name")
                .build();

        Product existingProduct = Product.builder()
                .id(productId)
                .productName("Product One")
                .stock(10)
                .build();


        when(productRepository.findById(productId)).thenReturn(Mono.just(existingProduct));
        when(productRepository.save(any(Product.class))).thenReturn(Mono.just(new Product(productId, newNameDTO.getNewNameDTO(), 10)));

        Mono<Product> result = productService.updateProductName(productId, newNameDTO);

        StepVerifier.create(result)
                .expectNextMatches(p -> p.getProductName().equals(newNameDTO.getNewNameDTO()))
                .verifyComplete();
    }


    @Test
    void testDeleteProductById() {

        String productId = "product1";

        when(productRepository.deleteById(productId)).thenReturn(Mono.empty());

        Mono<Void> result = productService.deleteProductById(productId);

        StepVerifier.create(result)

                .verifyComplete();
    }
}
