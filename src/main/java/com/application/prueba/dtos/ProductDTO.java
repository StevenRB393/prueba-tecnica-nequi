package com.application.prueba.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductDTO {

    @NotNull(message = "Id cannot be null")
    private String idDTO;

    @NotBlank(message = "Product name cannot be null")
    @Size(min = 2, max = 50, message = "Product name must be between 2 and 50 characters")
    @Indexed(unique = true)
    private String productNameDTO;

    @PositiveOrZero(message = "Stock cannot be negative")
    private int stockDTO;
}
