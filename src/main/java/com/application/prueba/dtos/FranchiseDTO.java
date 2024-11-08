package com.application.prueba.dtos;


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

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class FranchiseDTO {

    @NotNull(message = "Id cannot be null")
    private String idDTO;

    @NotBlank(message = "Franchise name cannot be null")
    @Size(min = 2, max = 50, message = "Franchise name must be between 2 and 50 characters")
    @Indexed(unique = true)
    private String franchiseNameDTO;

    @NotNull(message = "Store list cannot be null")
    @Size(min = 1, message = "There must be at least one store in the list")
    @Valid
    private List<StoreDTO> storeDTOList;
}
