package com.application.prueba.validators;

import com.application.prueba.dtos.FranchiseDTO;
import com.application.prueba.dtos.NewNameDTO;
import com.application.prueba.dtos.StoreDTO;
import com.application.prueba.exceptions.BadRequestException;
import com.application.prueba.exceptions.InvalidDataException;

public class FranchiseValidations {

    public static void validateFranchiseId(String franchiseId) {

        if (franchiseId == null || franchiseId.isBlank()) {
            throw new InvalidDataException("Franchise ID must not be null or empty");
        }
    }

    public static void validateFranchiseDTO(FranchiseDTO franchiseDTO) {

        if (franchiseDTO == null) {
            throw new BadRequestException("Franchise must not be null");
        }

        if (franchiseDTO.getFranchiseNameDTO() == null || franchiseDTO.getFranchiseNameDTO().isBlank()) {
            throw new InvalidDataException("Franchise name must not be null or empty");
        }
    }

    public static void validateStoreDTO(StoreDTO storeDTO) {

        if (storeDTO == null) {
            throw new BadRequestException("Store must not be null");
        }

        if (storeDTO.getStoreNameDTO() == null || storeDTO.getStoreNameDTO().isBlank()) {
            throw new InvalidDataException("Store name must not be null or empty");
        }
    }

    public static void validateNewNameDTO(NewNameDTO newNameDTO) {

        if (newNameDTO == null) {
            throw new BadRequestException("New name must not be null");
        }

        if (newNameDTO.getNewNameDTO() == null || newNameDTO.getNewNameDTO().isBlank()) {
            throw new InvalidDataException("New franchise name must not be null or empty");
        }
    }
}
