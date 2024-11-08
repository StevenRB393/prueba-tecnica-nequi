package com.application.prueba.validators;

import com.application.prueba.dtos.NewNameDTO;
import com.application.prueba.dtos.NewStockDTO;
import com.application.prueba.dtos.ProductDTO;
import com.application.prueba.exceptions.BadRequestException;
import com.application.prueba.exceptions.InvalidDataException;

public class ProductValidations {

    public static void validateProductId(String productId) {

        if (productId == null || productId.isBlank()) {
            throw new BadRequestException("Product ID must not be null or empty");
        }
    }

    public static void validateNewStockDTO(NewStockDTO newStockDTO) {

        if (newStockDTO == null) {
            throw new BadRequestException("Stock information must not be null");
        }

        if (newStockDTO.getNewStockDTO() < 0) {
            throw new InvalidDataException("Stock value must be a positive integer");
        }
    }

    public static void validateNewNameDTO(NewNameDTO newNameDTO) {

        if (newNameDTO == null || newNameDTO.getNewNameDTO() == null || newNameDTO.getNewNameDTO().isBlank()) {
            throw new BadRequestException("New product name must not be null or empty");
        }
    }

    public static void validateProductDTO(ProductDTO productDTO) {

        if (productDTO == null) {
            throw new BadRequestException("Product DTO must not be null");
        }

        if (productDTO.getProductNameDTO() == null || productDTO.getProductNameDTO().isBlank()) {
            throw new BadRequestException("Product name must not be null or empty");
        }

        if (productDTO.getStockDTO() < 0) {
            throw new InvalidDataException("Stock value must be a positive integer");
        }
    }
}
