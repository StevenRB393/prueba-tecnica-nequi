package com.application.prueba.models;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Document(collection = "stores")
public class Store {

    @Id
    @NotNull (message = "Id cannot be null")
    private String id;

    @NotBlank(message = "Store name cannot be null")
    @Size(min = 2, max = 50, message = "Store name must be between 2 and 50 characters")
    @Indexed(unique = true)
    private String storeName;

    @NotNull(message = "Product list cannot be null")
    @Size(min = 1, message = "There must be at least 1 product in the list")
    @Valid
    private List<Product> productList;
}
