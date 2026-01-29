package com.accenture.franchise.franchise_reactive_api.application.service;

import com.accenture.franchise.franchise_reactive_api.common.exception.DuplicateBranchException;
import com.accenture.franchise.franchise_reactive_api.domain.model.Branch;
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
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AddBranchToFranchiseServiceTest {

    @Mock
    private IFranchiseRepository repository;

    @InjectMocks
    private AddBranchToFranchiseService service;

    private final String FRANCHISE_ID = "franchise-123";
    private final String BRANCH_NAME = "New Branch";

    @Test
    void shouldThrowException_WhenBranchAlreadyExists() {
        // Arrange
        Branch existingBranch = new Branch(BRANCH_NAME, List.of());
        Franchise franchise = new Franchise(FRANCHISE_ID, "Tech Franchise", List.of(existingBranch));

        when(repository.findById(FRANCHISE_ID)).thenReturn(Mono.just(franchise));

        // Act & Assert
        StepVerifier.create(service.addBranch(FRANCHISE_ID, BRANCH_NAME))
                .expectError(DuplicateBranchException.class)
                .verify();

        verify(repository, never()).save(any(Franchise.class));
        assert franchise.getBranches().size() == 1; // No new branch added
    }

    @Test
    void shouldThrowException_WhenBranchNameExistsWithDifferentCase() {
        // Arrange
        Branch existingBranch = new Branch("existing branch", List.of());
        Franchise franchise = new Franchise(FRANCHISE_ID, "Tech Franchise", List.of(existingBranch));

        when(repository.findById(FRANCHISE_ID)).thenReturn(Mono.just(franchise));

        // Act & Assert
        StepVerifier.create(service.addBranch(FRANCHISE_ID, "EXISTING BRANCH"))
                .expectError(DuplicateBranchException.class)
                .verify();

        verify(repository, never()).save(any(Franchise.class));
    }

    @Test
    void shouldCompleteWithError_WhenFranchiseNotFound() {
        // Arrange
        when(repository.findById(FRANCHISE_ID)).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.addBranch(FRANCHISE_ID, BRANCH_NAME))
                .verifyComplete();

        verify(repository, never()).save(any(Franchise.class));
    }

    @Test
    void shouldHandleExceptionMessage() {
        // Arrange
        Branch existingBranch = new Branch(BRANCH_NAME, List.of());
        Franchise franchise = new Franchise(FRANCHISE_ID, "Tech Franchise", List.of(existingBranch));

        when(repository.findById(FRANCHISE_ID)).thenReturn(Mono.just(franchise));

        // Act & Assert
        StepVerifier.create(service.addBranch(FRANCHISE_ID, BRANCH_NAME))
                .expectErrorMatches(throwable -> 
                    throwable instanceof DuplicateBranchException &&
                    throwable.getMessage().contains("Branch with name '" + BRANCH_NAME + "' already exists")
                )
                .verify();
    }
}
