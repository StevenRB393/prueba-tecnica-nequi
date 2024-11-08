package com.application.prueba.mappers;

import com.application.prueba.dtos.ProductDTO;
import com.application.prueba.models.Product;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProductMapper {

    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    @Mapping(source = "id", target = "idDTO")
    @Mapping(source = "productName", target = "productNameDTO")
    @Mapping(source = "stock", target = "stockDTO")
    ProductDTO productToProductDTO(Product product);

    @InheritInverseConfiguration
    Product productDTOToProduct(ProductDTO productDTO);
}
