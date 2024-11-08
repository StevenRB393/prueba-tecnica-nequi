package com.application.prueba.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProductWithScoreDTO {

    private String storeNameDTO;

    private String productNameDTO;

    private int stockDTO;
}
