package com.accenture.franchise.franchise_reactive_api.api.handler;

import com.accenture.franchise.franchise_reactive_api.api.dto.CreateBranchRequest;
import com.accenture.franchise.franchise_reactive_api.api.dto.CreateFranchiseRequest;
import com.accenture.franchise.franchise_reactive_api.api.dto.CreateProductRequest;
import com.accenture.franchise.franchise_reactive_api.api.dto.TopStockProductResponse;
import com.accenture.franchise.franchise_reactive_api.api.dto.UpdateStockRequest;
import com.accenture.franchise.franchise_reactive_api.application.service.AddBranchToFranchiseService;
import com.accenture.franchise.franchise_reactive_api.application.service.AddProductToBranchService;
import com.accenture.franchise.franchise_reactive_api.application.service.CreateFranchiseService;
import com.accenture.franchise.franchise_reactive_api.application.service.DeleteProductFromBranchService;
import com.accenture.franchise.franchise_reactive_api.application.service.GetTopStockProductsByFranchiseService;
import com.accenture.franchise.franchise_reactive_api.application.service.UpdateProductStockService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FranchiseHandlerTest {

    @Mock
    private CreateFranchiseService createFranchiseService;

    @Mock
    private AddBranchToFranchiseService addBranchToFranchiseService;

    @Mock
    private AddProductToBranchService addProductToBranchService;

    @Mock
    private UpdateProductStockService updateProductStockService;

    @Mock
    private DeleteProductFromBranchService deleteProductFromBranchService;

    @Mock
    private GetTopStockProductsByFranchiseService getTopStockProductsByFranchiseService;

    @InjectMocks
    private FranchiseHandler handler;

    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        webTestClient = WebTestClient.bindToRouterFunction(
                new com.accenture.franchise.franchise_reactive_api.api.router.FranchiseRouter()
                        .franchiseRoutes(handler)
        ).build();
    }

    @Test
    void shouldReturn200OK_WhenGetTopStockProductsReturnsList() {
        // Arrange
        String franchiseId = "franchise-123";
        List<TopStockProductResponse> expectedResponse = List.of(
                new TopStockProductResponse("Branch-1", "Laptop", 100),
                new TopStockProductResponse("Branch-2", "Mouse", 50)
        );

        when(getTopStockProductsByFranchiseService.getTopStockByFranchise(franchiseId))
                .thenReturn(Mono.just(expectedResponse));

        // Act & Assert
        webTestClient.get()
                .uri("/franchises/{franchiseId}/branches/products/top-stock", franchiseId)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(TopStockProductResponse.class)
                .hasSize(2)
                .contains(expectedResponse.toArray(new TopStockProductResponse[0]));
    }

    @Test
    void shouldReturn404_WhenGetTopStockProductsReturnsEmptyList() {
        // Arrange
        String franchiseId = "franchise-123";

        when(getTopStockProductsByFranchiseService.getTopStockByFranchise(franchiseId))
                .thenReturn(Mono.just(List.of()));

        // Act & Assert
        webTestClient.get()
                .uri("/franchises/{franchiseId}/branches/products/top-stock", franchiseId)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void shouldReturn201Created_WhenAddBranchSucceeds() {
        // Arrange
        String franchiseId = "franchise-123";
        CreateBranchRequest request = new CreateBranchRequest("New Branch");

        when(addBranchToFranchiseService.addBranch(anyString(), anyString()))
                .thenReturn(Mono.empty());

        // Act & Assert
        webTestClient.post()
                .uri("/franchises/{franchiseId}/branches", franchiseId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated();
    }

    @Test
    void shouldReturn201Created_WhenAddProductSucceeds() {
        // Arrange
        String franchiseId = "franchise-123";
        String branchName = "Main Branch";
        CreateProductRequest request = new CreateProductRequest("Laptop", 50);

        when(addProductToBranchService.addProduct(anyString(), anyString(), anyString(), anyInt()))
                .thenReturn(Mono.empty());

        // Act & Assert
        webTestClient.post()
                .uri("/franchises/{franchiseId}/branches/{branchName}/products", franchiseId, branchName)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated();
    }

    @Test
    void shouldReturn200OK_WhenUpdateProductStockSucceeds() {
        // Arrange
        String franchiseId = "franchise-123";
        String branchName = "Main Branch";
        String productName = "Laptop";
        UpdateStockRequest request = new UpdateStockRequest(100);

        when(updateProductStockService.updateStock(anyString(), anyString(), anyString(), anyInt()))
                .thenReturn(Mono.empty());

        // Act & Assert
        webTestClient.put()
                .uri("/franchises/{franchiseId}/branches/{branchName}/products/{productName}/stock", 
                     franchiseId, branchName, productName)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void shouldReturn204NoContent_WhenDeleteProductSucceeds() {
        // Arrange
        String franchiseId = "franchise-123";
        String branchName = "Main Branch";
        String productName = "Laptop";

        when(deleteProductFromBranchService.delete(anyString(), anyString(), anyString()))
                .thenReturn(Mono.empty());

        // Act & Assert
        webTestClient.delete()
                .uri("/franchises/{franchiseId}/branches/{branchName}/products/{productName}", 
                     franchiseId, branchName, productName)
                .exchange()
                .expectStatus().isNoContent();
    }
}
