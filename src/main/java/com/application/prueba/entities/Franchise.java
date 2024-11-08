package com.application.prueba.entities;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Document(collection = "franchises")
public class Franchise {

    @Id
    @NotNull(message = "Id cannot be null")
    private String id;

    @NotBlank(message = "Franchise name cannot be null")
    @Size(min = 2, max = 50, message = "Franchise name must be between 2 and 50 characters")
    @Indexed(unique = true)
    private String franchiseName;

    @NotNull(message = "Store list cannot be null")
    @Size(min = 1, message = "There must be at least one store in the list")
    @Valid
    private List<Store> storeList;
}
