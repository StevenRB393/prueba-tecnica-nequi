package com.application.prueba.services.impl;

import com.application.prueba.dtos.NewNameDTO;
import com.application.prueba.dtos.NewStockDTO;
import com.application.prueba.dtos.ProductDTO;
import com.application.prueba.entities.Product;
import com.application.prueba.exceptions.NotFoundException;
import com.application.prueba.mappers.ProductMapper;
import com.application.prueba.repositories.ProductRepository;
import com.application.prueba.repositories.StoreRepository;
import com.application.prueba.services.ProductService;
import com.application.prueba.utils.ProductValidations;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    private final StoreRepository storeRepository;

    @Override
    public Mono<Product> findProductById(String productId) {

        ProductValidations.validateProductId(productId);

        return productRepository.findById(productId);
    }

    @Override
    public Flux<Product> findAllProducts() {

        return productRepository.findAll();
    }

    @Override
    public Mono<ProductDTO> saveProduct(ProductDTO productDTO) {

        ProductValidations.validateProductDTO(productDTO);

        Product product = ProductMapper.INSTANCE.productDTOToProduct(productDTO);

        return productRepository.save(product)
                .map(ProductMapper.INSTANCE::productToProductDTO);
    }

    @Override
    public Mono<Product> modifyProductStock(String productId, NewStockDTO newStockDTO) {

        ProductValidations.validateProductId(productId);

        ProductValidations.validateNewStockDTO(newStockDTO);

        return productRepository.findById(productId)
                .flatMap(product -> {
                    product.setStock(newStockDTO.getNewStockDTO());

                    return productRepository.save(product);
                });
    }

    @Override
    public Mono<Product> updateProductName(String productId, NewNameDTO newNameDTO) {

        ProductValidations.validateProductId(productId);

        ProductValidations.validateNewNameDTO(newNameDTO);

        return productRepository.findById(productId)
                .flatMap(oldName -> {
                    oldName.setProductName(newNameDTO.getNewNameDTO());

                    return productRepository.save(oldName);
                });
    }

    @Override
    public Mono<Void> deleteProductById(String productId) {

        ProductValidations.validateProductId(productId);

        return productRepository.deleteById(productId);
    }
}
