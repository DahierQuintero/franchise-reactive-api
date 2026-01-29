package com.accenture.franchise.franchise_reactive_api.application.service;

import com.accenture.franchise.franchise_reactive_api.domain.model.Branch;
import com.accenture.franchise.franchise_reactive_api.domain.model.Franchise;
import com.accenture.franchise.franchise_reactive_api.domain.model.Product;
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
class AddProductToBranchServiceTest {

    @Mock
    private IFranchiseRepository repository;

    @InjectMocks
    private AddProductToBranchService service;

    private final String FRANCHISE_ID = "franchise-123";
    private final String BRANCH_NAME = "Main Branch";
    private final String PRODUCT_NAME = "Laptop";
    private final int STOCK = 50;

    @Test
    void shouldCompleteWithoutAdding_WhenBranchDoesNotExist() {
        // Arrange
        Branch existingBranch = new Branch("Other Branch", List.of());
        Franchise franchise = new Franchise(FRANCHISE_ID, "Tech Franchise", List.of(existingBranch));

        when(repository.findById(FRANCHISE_ID)).thenReturn(Mono.just(franchise));

        // Act & Assert
        StepVerifier.create(service.addProduct(FRANCHISE_ID, "Non-Existent Branch", PRODUCT_NAME, STOCK))
                .verifyComplete();

        verify(repository, never()).save(any(Franchise.class));
    }

    @Test
    void shouldCompleteWithoutAdding_WhenFranchiseNotFound() {
        // Arrange
        when(repository.findById(FRANCHISE_ID)).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.addProduct(FRANCHISE_ID, BRANCH_NAME, PRODUCT_NAME, STOCK))
                .verifyComplete();

        verify(repository, never()).save(any(Franchise.class));
    }
}
