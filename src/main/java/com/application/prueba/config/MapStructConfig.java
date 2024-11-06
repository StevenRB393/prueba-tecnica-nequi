package com.application.prueba.config;

import com.application.prueba.mappers.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapStructConfig {

    @Bean
    public FranchiseMapper franchiseMapper() {
        return new FranchiseMapperImpl();
    }

    @Bean
    public StoreMapper storeMapper() {
        return new StoreMapperImpl();
    }

    @Bean
    public ProductMapper productMapper() {
        return new ProductMapperImpl();
    }
}
