package com.application.prueba.mappers;

import com.application.prueba.dtos.ProductDTO;
import com.application.prueba.entities.Product;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ProductMapper {

    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    @Mapping(source = "id", target = "idDTO")
    @Mapping(source = "productName", target = "productNameDTO")
    @Mapping(source = "stock", target = "stockDTO")
    ProductDTO productToProductDTO(Product product);

    List<ProductDTO> productListToProductDTOList(List<Product> productList);

    @InheritInverseConfiguration
    Product productDTOToProduct(ProductDTO productDTO);


    List<Product> productDTOListToProductList(List<ProductDTO> productDTOList);

}
