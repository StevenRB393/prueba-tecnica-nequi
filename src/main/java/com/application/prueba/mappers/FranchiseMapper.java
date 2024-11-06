package com.application.prueba.mappers;

import com.application.prueba.dtos.FranchiseDTO;
import com.application.prueba.dtos.ProductDTO;
import com.application.prueba.dtos.StoreDTO;
import com.application.prueba.entities.Franchise;
import com.application.prueba.entities.Product;
import com.application.prueba.entities.Store;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface FranchiseMapper {

    FranchiseMapper INSTANCE = Mappers.getMapper(FranchiseMapper.class);

    @Mapping(source = "id", target = "idDTO")
    @Mapping(source = "franchiseName", target = "franchiseNameDTO")
    @Mapping(source = "storeList", target = "storeDTOList")
    FranchiseDTO franchiseToFranchiseDTO(Franchise franchise);

    @InheritInverseConfiguration
    Franchise franchiseDTOToFranchise(FranchiseDTO franchiseDTO);

    @Mapping(target = "idDTO", source = "id")
    @Mapping(target = "storeNameDTO", source = "storeName")
    @Mapping(target = "productDTOList", source = "productList")
    StoreDTO storeToStoreDTO(Store store);

    @InheritInverseConfiguration
    Store storeDTOToStore(StoreDTO storeDTO);

    @Mapping(target = "idDTO", source = "id")
    @Mapping(target = "productNameDTO", source = "productName")
    @Mapping(target = "stockDTO", source = "stock")
    ProductDTO productToProductDTO(Product product);

    @InheritInverseConfiguration
    Product productDTOToProduct(ProductDTO productDTO);




}
