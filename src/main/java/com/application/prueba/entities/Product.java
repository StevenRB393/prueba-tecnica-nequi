package com.application.prueba.entities;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "products")
public class Product {

    @Id
    @NotNull(message = "Id cannot be null")
    private String id;

    @NotBlank(message = "Product name cannot be null")
    @Size(min = 2, max = 50, message = "Product name must be between 2 and 50 characters")
    @Indexed(unique = true)
    private String productName;

    @PositiveOrZero(message = "Stock cannot be negative")
    private int stock;

    /*public Product(String id, String productName, int stock) {
        this.id = id;
        this.productName = productName;
        this.stock = stock;
    }*/
}
