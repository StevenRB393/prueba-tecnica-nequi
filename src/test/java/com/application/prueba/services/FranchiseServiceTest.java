package com.application.prueba.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

import com.application.prueba.dtos.*;
import com.application.prueba.entities.Franchise;
import com.application.prueba.entities.Product;
import com.application.prueba.repositories.FranchiseRepository;
import com.application.prueba.repositories.ProductRepository;
import com.application.prueba.services.impl.FranchiseServiceImpl;
import com.application.prueba.services.impl.ProductServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import java.util.ArrayList;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FranchiseServiceTest {

    @Mock
    private FranchiseRepository franchiseRepository;


    @InjectMocks
    private FranchiseServiceImpl franchiseService;

    @Test
    void testFindFranchiseById() {
        String franchiseId = "franchise1";
        Franchise expectedFranchise = new Franchise(franchiseId, "Franchise One", new ArrayList<>());

        // Mock para el comportamiento de findById
        when(franchiseRepository.findById(franchiseId)).thenReturn(Mono.just(expectedFranchise));

        // Act
        Mono<Franchise> result = franchiseService.findFranchiseById(franchiseId); // Asegúrate de que esto sea Mono<Franchise>

        // Assert
        StepVerifier.create(result)
                .expectNext(expectedFranchise) // Verifica que el resultado sea la franquicia esperada
                .verifyComplete(); // Verifica que la operación se complete sin errores

        // Verifica que se haya llamado findById con el ID correcto
        verify(franchiseRepository, times(1)).findById(franchiseId);
    }

    @Test
    void testFindAllFranchises() {

        Franchise franchise1 = new Franchise("franchise1", "Franchise One", new ArrayList<>());
        Franchise franchise2 = new Franchise("franchise2", "Franchise Two", new ArrayList<>());
        Flux<Franchise> franchiseFlux = Flux.just(franchise1, franchise2);

        // Mock para el comportamiento de findAll
        when(franchiseRepository.findAll()).thenReturn(franchiseFlux);

        // Act
        Flux<Franchise> result = franchiseService.findAllFranchises();

        // Assert
        StepVerifier.create(result)
                .expectNext(franchise1) // Verifica que se emita la primera franquicia
                .expectNext(franchise2) // Verifica que se emita la segunda franquicia
                .verifyComplete(); // Verifica que se complete el flujo

        // Verifica que se haya llamado findAll una vez
        verify(franchiseRepository, times(1)).findAll();

    }

    @Test
    void testSaveFranchise() {
        FranchiseDTO franchiseDTO = new FranchiseDTO("franchise1", "Franchise One", null);
        Franchise franchise = new Franchise();
        franchise.setId(franchiseDTO.getIdDTO());
        franchise.setFranchiseName(franchiseDTO.getFranchiseNameDTO());

        // Configurar el comportamiento del mock
        when(franchiseRepository.save(any(Franchise.class))).thenReturn(Mono.just(franchise));

        // Ejecutar el método
        Mono<Franchise> result = franchiseService.saveFranchise(franchiseDTO);

        // Verificar resultados
        result.subscribe(savedFranchise -> {
            assertNotNull(savedFranchise);
            assertEquals(franchise.getId(), savedFranchise.getId());
            assertEquals(franchise.getFranchiseName(), savedFranchise.getFranchiseName());
        });

        // Verificar que el repositorio fue llamado
        verify(franchiseRepository, times(1)).save(any(Franchise.class));
    }

    @Test
    void saveStoreByFranchisie() {

        StoreDTO storeDTO = new StoreDTO();
        storeDTO.setIdDTO("storeId1");
        storeDTO.setStoreNameDTO("Store One");
        storeDTO.setProductDTOList(new ArrayList<>()); // Lista de productos vacía

        // Crear un objeto Franchise simulado
        Franchise franchise = new Franchise();
        franchise.setId("franchiseId1");
        franchise.setFranchiseName("Franchise One");
        franchise.setStoreList(new ArrayList<>()); // Inicializar la lista de tiendas

        // Definir el comportamiento simulado del repositorio
        when(franchiseRepository.findById("franchiseId1")).thenReturn(Mono.just(franchise));
        when(franchiseRepository.save(any(Franchise.class))).thenReturn(Mono.just(franchise));

        // Llamar al método del servicio
        Mono<Franchise> result = franchiseService.saveStoreByFranchise("franchiseId1", storeDTO);

        // Verificar el resultado
        StepVerifier.create(result)
                .expectNext(franchise) // Espera que devuelva la franquicia con la tienda agregada
                .verifyComplete();

        // Verificar que se llamaron los métodos del repositorio
        verify(franchiseRepository).findById("franchiseId1");
        verify(franchiseRepository).save(franchise);

    }

    @Test
    void testUpdateNameByFranchisie() {
        String franchiseId = "franchise1";
        String newName = "Updated Franchise Name";
        NewNameDTO newNameDTO = new NewNameDTO(newName);

        Franchise existingFranchise = new Franchise();
        existingFranchise.setId(franchiseId);
        existingFranchise.setFranchiseName("Old Franchise Name");
        existingFranchise.setStoreList(new ArrayList<>());

        // Simulamos el comportamiento del repositorio
        when(franchiseRepository.findById(franchiseId)).thenReturn(Mono.just(existingFranchise));
        when(franchiseRepository.save(any(Franchise.class))).thenAnswer(invocation -> {
            Franchise savedFranchise = invocation.getArgument(0);
            // Simular la actualización del nombre
            savedFranchise.setFranchiseName(newName);
            return Mono.just(savedFranchise);
        });

        // Llamamos al método
        Mono<Franchise> result = franchiseService.updateFranchiseName(franchiseId, newNameDTO);

        // Verificamos el resultado
        StepVerifier.create(result)
                .expectNextMatches(franchise -> franchise.getFranchiseName().equals(newName))
                .verifyComplete();

        // Verificamos que el repositorio haya sido llamado
        verify(franchiseRepository).findById(franchiseId);
        verify(franchiseRepository).save(existingFranchise);

    }

    @Test
    void testDeleteFranchiseById() {
        String franchiseId = "franchise1";
        doReturn(Mono.empty()).when(franchiseRepository).deleteById(franchiseId); // Simulando que deleteById devuelve Mono vacío.

        // Act
        Mono<Void> result = franchiseService.deleteFranchiseById(franchiseId);

        // Assert
        StepVerifier.create(result)
                .expectComplete() // Verifica que se complete sin errores
                .verify();

        // Verificar que el método deleteById fue llamado con el ID correcto.
        verify(franchiseRepository, times(1)).deleteById(franchiseId);

    }
}
