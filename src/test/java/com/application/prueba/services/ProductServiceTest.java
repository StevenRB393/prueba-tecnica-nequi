package com.application.prueba.services;

import static org.mockito.Mockito.*;
import com.application.prueba.dtos.NewNameDTO;
import com.application.prueba.dtos.NewStockDTO;
import com.application.prueba.dtos.ProductDTO;
import com.application.prueba.entities.Product;
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
import java.util.Arrays;
import java.util.List;
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
        Product product = new Product(productId, "Product One", 10);

        when(productRepository.findById(productId)).thenReturn(Mono.just(product));

        Mono<Product> result = productService.findProductById(productId);

        StepVerifier.create(result)
                .expectNextMatches(p -> p.getId().equals(productId) && p.getProductName().equals("Product One"))
                .verifyComplete();
    }

    @Test
    void testFindAllProducts() {
        Product product1 = new Product("product1", "Product One", 10);
        Product product2 = new Product("product2", "Product Two", 20);
        List<Product> productList = Arrays.asList(product1, product2);

        when(productRepository.findAll()).thenReturn(Flux.just(product1, product2));

        Flux<Product> result = productService.findAllProducts();

        StepVerifier.create(result)
                .expectNext(product1)
                .expectNext(product2)
                .verifyComplete();
    }

    @Test
    void testSaveProduct() {
        ProductDTO productDTO = new ProductDTO("product1", "Product One", 10);
        Product product = new Product(productDTO.getIdDTO(), productDTO.getProductNameDTO(), productDTO.getStockDTO());

        when(productRepository.save(any(Product.class))).thenReturn(Mono.just(product));

        Mono<ProductDTO> result = productService.saveProduct(productDTO);

        StepVerifier.create(result)
                .expectNextMatches(p -> p.getIdDTO().equals("product1") && p.getProductNameDTO().equals("Product One"))
                .verifyComplete();
    }

    @Test
    void testModifyProductStock() {
        String productId = "product1";
        NewStockDTO newStockDTO = new NewStockDTO(15);
        Product existingProduct = new Product(productId, "Product Name", 10);

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
        NewNameDTO newNameDTO = new NewNameDTO("New Product Name");
        Product existingProduct = new Product(productId, "Product One", 10);

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
