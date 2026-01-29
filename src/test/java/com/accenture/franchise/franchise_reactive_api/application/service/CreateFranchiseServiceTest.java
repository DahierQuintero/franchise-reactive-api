package com.accenture.franchise.franchise_reactive_api.application.service;

import com.accenture.franchise.franchise_reactive_api.domain.model.Franchise;
import com.accenture.franchise.franchise_reactive_api.domain.repository.IFranchiseRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateFranchiseServiceTest {

    @Mock
    private IFranchiseRepository repository;

    @InjectMocks
    private CreateFranchiseService service;

    private final String FRANCHISE_NAME = "Tech Franchise";

    @Test
    void shouldCreateFranchise_WhenValidNameProvided() {
        // Arrange
        Franchise expectedFranchise = new Franchise(FRANCHISE_NAME, List.of());
        when(repository.save(any(Franchise.class))).thenReturn(Mono.just(expectedFranchise));

        // Act & Assert
        StepVerifier.create(service.create(FRANCHISE_NAME))
                .expectNext(expectedFranchise)
                .verifyComplete();

        verify(repository).save(any(Franchise.class));
    }

    @Test
    void shouldCreateFranchiseWithEmptyBranchList_WhenNameProvided() {
        // Arrange
        when(repository.save(any(Franchise.class))).thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

        // Act & Assert
        StepVerifier.create(service.create(FRANCHISE_NAME))
                .assertNext(franchise -> {
                    assert franchise.getName().equals(FRANCHISE_NAME);
                    assert franchise.getBranches().isEmpty();
                    assert franchise.getId() != null; // ID should be generated
                })
                .verifyComplete();

        verify(repository).save(any(Franchise.class));
    }

    @Test
    void shouldPropagateError_WhenRepositorySaveFails() {
        // Arrange
        RuntimeException error = new RuntimeException("Database error");
        when(repository.save(any(Franchise.class))).thenReturn(Mono.error(error));

        // Act & Assert
        StepVerifier.create(service.create(FRANCHISE_NAME))
                .expectError(RuntimeException.class)
                .verify();

        verify(repository).save(any(Franchise.class));
    }

    @Test
    void shouldCreateFranchiseWithUniqueId_WhenCalledMultipleTimes() {
        // Arrange
        when(repository.save(any(Franchise.class))).thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

        // Act & Assert
        Mono<Franchise> firstFranchiseMono = service.create(FRANCHISE_NAME);
        Mono<Franchise> secondFranchiseMono = service.create(FRANCHISE_NAME);

        StepVerifier.create(firstFranchiseMono.zipWith(secondFranchiseMono))
                .assertNext(tuple -> {
                    Franchise first = tuple.getT1();
                    Franchise second = tuple.getT2();
                    
                    assert first.getName().equals(second.getName());
                    assert !first.getId().equals(second.getId()); // IDs should be different
                })
                .verifyComplete();
    }

    @Test
    void shouldHandleEmptyName() {
        // Arrange
        String emptyName = "";
        when(repository.save(any(Franchise.class))).thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

        // Act & Assert
        StepVerifier.create(service.create(emptyName))
                .assertNext(franchise -> {
                    assert franchise.getName().equals(emptyName);
                    assert franchise.getBranches().isEmpty();
                })
                .verifyComplete();

        verify(repository).save(any(Franchise.class));
    }

    @Test
    void shouldHandleNullName() {
        // Arrange
        String nullName = null;
        when(repository.save(any(Franchise.class))).thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

        // Act & Assert
        StepVerifier.create(service.create(nullName))
                .assertNext(franchise -> {
                    assert franchise.getName() == null;
                    assert franchise.getBranches().isEmpty();
                })
                .verifyComplete();

        verify(repository).save(any(Franchise.class));
    }

    @Test
    void shouldCreateFranchiseSuccessfully_WhenRepositoryReturnsEmptyMono() {
        // Arrange
        when(repository.save(any(Franchise.class))).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.create(FRANCHISE_NAME))
                .verifyComplete();

        verify(repository).save(any(Franchise.class));
    }
}
