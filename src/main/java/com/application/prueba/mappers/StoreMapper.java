package com.application.prueba.mappers;

import com.application.prueba.dtos.NewNameDTO;
import com.application.prueba.dtos.ProductDTO;
import com.application.prueba.dtos.StoreDTO;
import com.application.prueba.entities.Product;
import com.application.prueba.entities.Store;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface StoreMapper {

    StoreMapper INSTANCE = Mappers.getMapper(StoreMapper.class);

    @Mapping(source = "id", target = "idDTO")
    @Mapping(source = "storeName", target = "storeNameDTO")
    @Mapping(source = "productList", target = "productDTOList")
    StoreDTO storeToStoreDTO(Store store);

    @InheritInverseConfiguration
    Store storeDTOToStore(StoreDTO storeDTO);

    @Mapping(target = "idDTO", source = "id")
    @Mapping(target = "productNameDTO", source = "productName")
    @Mapping(target = "stockDTO", source = "stock")
    ProductDTO productToProductDTO(Product product);

    @InheritInverseConfiguration
    Product productDTOToProduct(ProductDTO productDTO);

    List<StoreDTO> storeListToStoreDTOList(List<Store> storeList);


    List<Store> storeDTOListToStoreList(List<StoreDTO> storeDTOList);
}
