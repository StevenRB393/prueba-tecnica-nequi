package com.application.prueba.utils;

import com.application.prueba.dtos.NewNameDTO;
import com.application.prueba.dtos.StoreDTO;
import com.application.prueba.exceptions.BadRequestException;
import com.application.prueba.exceptions.InvalidDataException;

public class StoreValidations {

    public static void validateStoreDTO(StoreDTO storeDTO) {

        if (storeDTO == null) {
            throw new BadRequestException("Store data must not be null");
        }

        if (storeDTO.getStoreNameDTO() == null || storeDTO.getStoreNameDTO().isBlank()) {
            throw new InvalidDataException("Store name must not be null or empty");
        }
    }

    public static void validateNewNameDTO(NewNameDTO newNameDTO) {

        if (newNameDTO == null) {
            throw new BadRequestException("New name data must not be null");
        }

        if (newNameDTO.getNewNameDTO() == null || newNameDTO.getNewNameDTO().isBlank()) {
            throw new InvalidDataException("New store name must not be null or empty");
        }
    }

    public static void validateStoreId(String storeId) {

        if (storeId == null || storeId.isBlank()) {
            throw new InvalidDataException("Store ID must not be null or empty");
        }
    }
}
