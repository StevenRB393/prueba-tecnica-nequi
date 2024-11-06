package com.application.prueba.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductWithScoreDTO {

    private String storeNameDTO;

    private String productNameDTO;

    private int stockDTO;
}
