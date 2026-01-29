package com.accenture.franchise.franchise_reactive_api.application.service;

import com.accenture.franchise.franchise_reactive_api.api.dto.TopStockProductResponse;
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

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetTopStockProductsByFranchiseServiceTest {

    @Mock
    private IFranchiseRepository repository;

    @InjectMocks
    private GetTopStockProductsByFranchiseService service;

    private final String FRANCHISE_ID = "franchise-123";

    @Test
    void shouldReturnTopStockProducts_WhenFranchiseHasMultipleBranchesAndProducts() {
        // Arrange
        Product product1 = new Product("Laptop", 50);
        Product product2 = new Product("Mouse", 100);
        Product product3 = new Product("Keyboard", 30);
        Product product4 = new Product("Monitor", 80);

        Branch branch1 = new Branch("Branch-1", List.of(product1, product2));
        Branch branch2 = new Branch("Branch-2", List.of(product3, product4));

        Franchise franchise = new Franchise(FRANCHISE_ID, "Tech Franchise", List.of(branch1, branch2));

        when(repository.findById(FRANCHISE_ID)).thenReturn(Mono.just(franchise));

        List<TopStockProductResponse> expected = List.of(
                new TopStockProductResponse("Branch-1", "Mouse", 100),
                new TopStockProductResponse("Branch-2", "Monitor", 80)
        );

        // Act & Assert
        StepVerifier.create(service.getTopStockByFranchise(FRANCHISE_ID))
                .expectNext(expected)
                .verifyComplete();
    }

    @Test
    void shouldIgnoreBranch_WhenBranchHasNoProducts() {
        // Arrange
        Branch branchWithProducts = new Branch("Branch-1", List.of(new Product("Laptop", 50)));
        Branch branchWithoutProducts = new Branch("Branch-2", List.of());

        Franchise franchise = new Franchise(FRANCHISE_ID, "Tech Franchise", List.of(branchWithProducts, branchWithoutProducts));

        when(repository.findById(FRANCHISE_ID)).thenReturn(Mono.just(franchise));

        List<TopStockProductResponse> expected = List.of(
                new TopStockProductResponse("Branch-1", "Laptop", 50)
        );

        // Act & Assert
        StepVerifier.create(service.getTopStockByFranchise(FRANCHISE_ID))
                .expectNext(expected)
                .verifyComplete();
    }

    @Test
    void shouldReturnEmptyList_WhenFranchiseHasNoBranches() {
        // Arrange
        Franchise franchise = new Franchise(FRANCHISE_ID, "Empty Franchise", List.of());

        when(repository.findById(FRANCHISE_ID)).thenReturn(Mono.just(franchise));

        // Act & Assert
        StepVerifier.create(service.getTopStockByFranchise(FRANCHISE_ID))
                .expectNext(List.of())
                .verifyComplete();
    }

    @Test
    void shouldReturnEmptyMono_WhenFranchiseNotFound() {
        // Arrange
        when(repository.findById(FRANCHISE_ID)).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.getTopStockByFranchise(FRANCHISE_ID))
                .verifyComplete();
    }

    @Test
    void shouldHandleMultipleProductsInSameBranch_WhenTheyHaveSameMaxStock() {
        // Arrange
        Product product1 = new Product("Laptop", 50);
        Product product2 = new Product("Mouse", 50);
        Product product3 = new Product("Keyboard", 30);

        Branch branch = new Branch("Branch-1", List.of(product1, product2, product3));

        Franchise franchise = new Franchise(FRANCHISE_ID, "Tech Franchise", List.of(branch));

        when(repository.findById(FRANCHISE_ID)).thenReturn(Mono.just(franchise));

        // Act & Assert - Should return only one of the products with max stock (max() behavior)
        StepVerifier.create(service.getTopStockByFranchise(FRANCHISE_ID))
                .expectNextMatches(list -> list.size() == 1 && 
                        list.get(0).stock() == 50 &&
                        "Branch-1".equals(list.get(0).branchName()))
                .verifyComplete();
    }
}
