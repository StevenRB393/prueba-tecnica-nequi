package com.application.prueba.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import com.application.prueba.dtos.*;
import com.application.prueba.models.Franchise;
import com.application.prueba.repositories.FranchiseRepository;
import com.application.prueba.services.impl.FranchiseServiceImpl;
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

        Franchise expectedFranchise = Franchise.builder()
                .id(franchiseId)
                .franchiseName("Franchise One")
                .storeList(new ArrayList<>())
                .build();

        when(franchiseRepository.findById(franchiseId)).thenReturn(Mono.just(expectedFranchise));

        Mono<Franchise> result = franchiseService.findFranchiseById(franchiseId);

        StepVerifier.create(result)
                .expectNext(expectedFranchise)
                .verifyComplete();

        verify(franchiseRepository, times(1)).findById(franchiseId);
    }

    @Test
    void testFindAllFranchises() {

        Franchise franchise1 = Franchise.builder()
                .id("franchise1")
                .franchiseName("Franchise One")
                .storeList(new ArrayList<>())
                .build();

        Franchise franchise2 = Franchise.builder()
                .id("franchise2")
                .franchiseName("Franchise Two")
                .storeList(new ArrayList<>())
                .build();

        Flux<Franchise> franchiseFlux = Flux.just(franchise1, franchise2);

        when(franchiseRepository.findAll()).thenReturn(franchiseFlux);

        Flux<Franchise> result = franchiseService.findAllFranchises();

        StepVerifier.create(result)
                .expectNext(franchise1)
                .expectNext(franchise2)

                .verifyComplete();

        verify(franchiseRepository, times(1)).findAll();
    }

    @Test
    void testSaveFranchise() {

        FranchiseDTO franchiseDTO = FranchiseDTO.builder()
                .idDTO("franchise1")
                .franchiseNameDTO("Franchise One")
                .storeDTOList(null)
                .build();

        Franchise franchise = Franchise.builder()
                .id("franchise1")
                .franchiseName("Franchise One")
                .storeList(new ArrayList<>())
                .build();

        when(franchiseRepository.save(any(Franchise.class))).thenReturn(Mono.just(franchise));

        Mono<Franchise> result = franchiseService.saveFranchise(franchiseDTO);

        result.subscribe(savedFranchise -> {
            assertNotNull(savedFranchise);
            assertEquals(franchise.getId(), savedFranchise.getId());
            assertEquals(franchise.getFranchiseName(), savedFranchise.getFranchiseName());
        });

        verify(franchiseRepository, times(1)).save(any(Franchise.class));
    }

    @Test
    void saveStoreByFranchise() {

        StoreDTO storeDTO = StoreDTO.builder()
                .idDTO("storeId1")
                .storeNameDTO("Store One")
                .productDTOList(new ArrayList<>())
                .build();

        Franchise franchise = Franchise.builder()
                .id("franchiseId1")
                .franchiseName("Franchise One")
                .storeList(new ArrayList<>())
                .build();

        when(franchiseRepository.findById("franchiseId1")).thenReturn(Mono.just(franchise));
        when(franchiseRepository.save(any(Franchise.class))).thenReturn(Mono.just(franchise));

        Mono<Franchise> result = franchiseService.saveStoreByFranchise("franchiseId1", storeDTO);

        StepVerifier.create(result)
                .expectNext(franchise)
                .verifyComplete();

        verify(franchiseRepository).findById("franchiseId1");
        verify(franchiseRepository).save(franchise);
    }

    @Test
    void testUpdateNameByFranchise() {

        String franchiseId = "franchise1";

        String newName = "Updated Franchise Name";

        NewNameDTO newNameDTO = NewNameDTO.builder()
                .newNameDTO(newName)
                .build();

        Franchise existingFranchise = Franchise.builder()
                .id(franchiseId)
                .franchiseName("Old Franchise Name")
                .storeList(new ArrayList<>())
                .build();

        when(franchiseRepository.findById(franchiseId)).thenReturn(Mono.just(existingFranchise));
        when(franchiseRepository.save(any(Franchise.class))).thenAnswer(invocation -> {
            Franchise savedFranchise = invocation.getArgument(0);
            savedFranchise.setFranchiseName(newName);
            return Mono.just(savedFranchise);
        });

        Mono<Franchise> result = franchiseService.updateFranchiseName(franchiseId, newNameDTO);

        StepVerifier.create(result)
                .expectNextMatches(franchise -> franchise.getFranchiseName().equals(newName))
                .verifyComplete();

        verify(franchiseRepository).findById(franchiseId);
        verify(franchiseRepository).save(existingFranchise);
    }

    @Test
    void testDeleteFranchiseById() {

        String franchiseId = "franchise1";

        doReturn(Mono.empty()).when(franchiseRepository).deleteById(franchiseId);

        Mono<Void> result = franchiseService.deleteFranchiseById(franchiseId);

        StepVerifier.create(result)
                .expectComplete()
                .verify();

        verify(franchiseRepository, times(1)).deleteById(franchiseId);
    }
}
