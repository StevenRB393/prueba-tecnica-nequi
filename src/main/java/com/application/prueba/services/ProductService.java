package com.application.prueba.services;

import com.application.prueba.dtos.NewNameDTO;
import com.application.prueba.dtos.NewStockDTO;
import com.application.prueba.dtos.ProductDTO;
import com.application.prueba.entities.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductService {

    Mono<Product> findProductById(String productId);

    Flux<Product> findAllProducts();

    Mono<ProductDTO> saveProduct(ProductDTO productDTO);

    Mono<Product> modifyProductStock(String productId, NewStockDTO newStockDTO);

    Mono<Product> updateProductName(String productId, NewNameDTO newNameDTO);

    Mono<Void> deleteProductById(String productId);
}
