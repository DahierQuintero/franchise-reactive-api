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
class UpdateProductStockServiceTest {

    @Mock
    private IFranchiseRepository repository;

    @InjectMocks
    private UpdateProductStockService service;

    private final String FRANCHISE_ID = "franchise-123";
    private final String BRANCH_NAME = "Main Branch";
    private final String PRODUCT_NAME = "Laptop";
    private final int NEW_STOCK = 100;

    @Test
    void shouldUpdateStock_WhenBranchAndProductExist() {
        // Arrange
        Product product = new Product(PRODUCT_NAME, 50);
        Branch branch = new Branch(BRANCH_NAME, List.of(product));
        Franchise franchise = new Franchise(FRANCHISE_ID, "Tech Franchise", List.of(branch));

        when(repository.findById(FRANCHISE_ID)).thenReturn(Mono.just(franchise));
        when(repository.save(any(Franchise.class))).thenReturn(Mono.just(franchise));

        // Act & Assert
        StepVerifier.create(service.updateStock(FRANCHISE_ID, BRANCH_NAME, PRODUCT_NAME, NEW_STOCK))
                .verifyComplete();

        verify(repository).save(franchise);
        assert product.getStock() == NEW_STOCK;
    }

    @Test
    void shouldCompleteWithoutUpdating_WhenBranchDoesNotExist() {
        // Arrange
        Product product = new Product(PRODUCT_NAME, 50);
        Branch existingBranch = new Branch("Other Branch", List.of(product));
        Franchise franchise = new Franchise(FRANCHISE_ID, "Tech Franchise", List.of(existingBranch));

        when(repository.findById(FRANCHISE_ID)).thenReturn(Mono.just(franchise));

        // Act & Assert
        StepVerifier.create(service.updateStock(FRANCHISE_ID, "Non-Existent Branch", PRODUCT_NAME, NEW_STOCK))
                .verifyComplete();

        verify(repository, never()).save(any(Franchise.class));
        assert product.getStock() == 50; // Stock unchanged
    }

    @Test
    void shouldCompleteWithoutUpdating_WhenProductDoesNotExist() {
        // Arrange
        Product existingProduct = new Product("Mouse", 30);
        Branch branch = new Branch(BRANCH_NAME, List.of(existingProduct));
        Franchise franchise = new Franchise(FRANCHISE_ID, "Tech Franchise", List.of(branch));

        when(repository.findById(FRANCHISE_ID)).thenReturn(Mono.just(franchise));

        // Act & Assert
        StepVerifier.create(service.updateStock(FRANCHISE_ID, BRANCH_NAME, "Non-Existent Product", NEW_STOCK))
                .verifyComplete();

        verify(repository, never()).save(any(Franchise.class));
        assert existingProduct.getStock() == 30; // Stock unchanged
    }

    @Test
    void shouldCompleteWithoutUpdating_WhenFranchiseNotFound() {
        // Arrange
        when(repository.findById(FRANCHISE_ID)).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.updateStock(FRANCHISE_ID, BRANCH_NAME, PRODUCT_NAME, NEW_STOCK))
                .verifyComplete();

        verify(repository, never()).save(any(Franchise.class));
    }

    @Test
    void shouldHandleCaseInsensitiveBranchAndProductNames() {
        // Arrange
        Product product = new Product("laptop", 50);
        Branch branch = new Branch("main branch", List.of(product));
        Franchise franchise = new Franchise(FRANCHISE_ID, "Tech Franchise", List.of(branch));

        when(repository.findById(FRANCHISE_ID)).thenReturn(Mono.just(franchise));
        when(repository.save(any(Franchise.class))).thenReturn(Mono.just(franchise));

        // Act & Assert
        StepVerifier.create(service.updateStock(FRANCHISE_ID, "MAIN BRANCH", "LAPTOP", NEW_STOCK))
                .verifyComplete();

        verify(repository).save(franchise);
        assert product.getStock() == NEW_STOCK;
    }

    @Test
    void shouldHandleBranchNameWithDashes() {
        // Arrange
        String branchNameWithDashes = "Main-Branch";
        String normalizedBranchName = "Main Branch";
        
        Product product = new Product(PRODUCT_NAME, 50);
        Branch branch = new Branch(normalizedBranchName, List.of(product));
        Franchise franchise = new Franchise(FRANCHISE_ID, "Tech Franchise", List.of(branch));

        when(repository.findById(FRANCHISE_ID)).thenReturn(Mono.just(franchise));
        when(repository.save(any(Franchise.class))).thenReturn(Mono.just(franchise));

        // Act & Assert
        StepVerifier.create(service.updateStock(FRANCHISE_ID, branchNameWithDashes, PRODUCT_NAME, NEW_STOCK))
                .verifyComplete();

        verify(repository).save(franchise);
        assert product.getStock() == NEW_STOCK;
    }

    @Test
    void shouldUpdateCorrectProduct_WhenMultipleProductsExist() {
        // Arrange
        Product product1 = new Product("Laptop", 50);
        Product product2 = new Product("Mouse", 30);
        Product product3 = new Product("Keyboard", 20);
        
        Branch branch = new Branch(BRANCH_NAME, List.of(product1, product2, product3));
        Franchise franchise = new Franchise(FRANCHISE_ID, "Tech Franchise", List.of(branch));

        when(repository.findById(FRANCHISE_ID)).thenReturn(Mono.just(franchise));
        when(repository.save(any(Franchise.class))).thenReturn(Mono.just(franchise));

        // Act & Assert
        StepVerifier.create(service.updateStock(FRANCHISE_ID, BRANCH_NAME, "Mouse", NEW_STOCK))
                .verifyComplete();

        verify(repository).save(franchise);
        assert product1.getStock() == 50;  // Unchanged
        assert product2.getStock() == NEW_STOCK;  // Updated
        assert product3.getStock() == 20;  // Unchanged
    }
}
