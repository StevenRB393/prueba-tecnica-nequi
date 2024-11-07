package com.application.prueba.controllers;

import com.application.prueba.dtos.NewNameDTO;
import com.application.prueba.dtos.NewStockDTO;
import com.application.prueba.dtos.ProductDTO;
import com.application.prueba.mappers.ProductMapper;
import com.application.prueba.services.ProductService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("api/products")
@RequiredArgsConstructor
@Tag(name= "Products")
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    private final ProductService productService;

    private final ProductMapper productMapper;

    @GetMapping("/{productId}")
    public Mono<ResponseEntity<ProductDTO>> getProductById(@PathVariable String productId) {
        logger.info("Request received to get product with ID: {}", productId);
        return productService.findProductById(productId)
                .map(product -> {
                    logger.info("Product found with ID: {}", productId);
                    ProductDTO productDTO = productMapper.productToProductDTO(product);
                    return ResponseEntity.ok(productDTO);
                })
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping
    public Flux<ResponseEntity<ProductDTO>> getAllProducts() {
        logger.info("Request received to get all products");
        return productService.findAllProducts()
                .map(product -> {
                    logger.info("Product found with ID: {}", product.getId());
                    ProductDTO responseDTO = productMapper.productToProductDTO(product);
                    return ResponseEntity.ok(responseDTO);
                });
    }

    @PostMapping
    public Mono<ResponseEntity<ProductDTO>> save(@RequestBody ProductDTO productDTO) {
        logger.info("Request received to save a new product");
        return productService.saveProduct(productDTO)
                .map(savedProductDTO -> {
                    logger.info("Product saved with ID: {}", savedProductDTO.getIdDTO());
                    return ResponseEntity.status(HttpStatus.CREATED).body(savedProductDTO);
                })
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PutMapping("/{productId}/stock")
    public Mono<ResponseEntity<ProductDTO>> updateStock(@PathVariable String productId, @RequestBody NewStockDTO newStockDTO) {
        logger.info("Request received to update stock for product ID: {}", productId);
        return productService.modifyProductStock(productId, newStockDTO)
                .map(updatedStock -> {
                    logger.info("Stock updated for product ID: {}", productId);
                    return ResponseEntity.ok(ProductMapper.INSTANCE.productToProductDTO(updatedStock));
                })
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PutMapping("/{productId}")
    public Mono<ResponseEntity<ProductDTO>> updateProductName(@PathVariable String productId, @RequestBody NewNameDTO newNameDTO) {
        logger.info("Request received to update product name for ID: {}", productId);
        return productService.updateProductName(productId, newNameDTO)
                .map(updatedName -> {
                    logger.info("Product name updated for ID: {}", productId);
                    return ResponseEntity.ok(ProductMapper.INSTANCE.productToProductDTO(updatedName));
                })
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{productId}")
    public Mono<ResponseEntity<Void>> deleteProduct(@PathVariable String productId) {
        logger.info("Request received to delete product with ID: {}", productId);
        return productService.deleteProductById(productId)
                .then(Mono.fromRunnable(() -> logger.info("Product deleted successfully: {}", productId)))
                .then(Mono.just(ResponseEntity.noContent().build()));
    }
}
