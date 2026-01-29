package com.accenture.franchise.franchise_reactive_api.api.handler;

import com.accenture.franchise.franchise_reactive_api.api.dto.CreateBranchRequest;
import com.accenture.franchise.franchise_reactive_api.api.dto.CreateFranchiseRequest;
import com.accenture.franchise.franchise_reactive_api.api.dto.CreateProductRequest;
import com.accenture.franchise.franchise_reactive_api.api.dto.FranchiseResponse;
import com.accenture.franchise.franchise_reactive_api.api.dto.UpdateStockRequest;
import com.accenture.franchise.franchise_reactive_api.application.service.AddBranchToFranchiseService;
import com.accenture.franchise.franchise_reactive_api.application.service.AddProductToBranchService;
import com.accenture.franchise.franchise_reactive_api.application.service.CreateFranchiseService;
import com.accenture.franchise.franchise_reactive_api.application.service.DeleteProductFromBranchService;
import com.accenture.franchise.franchise_reactive_api.application.service.GetTopStockProductsByFranchiseService;
import com.accenture.franchise.franchise_reactive_api.application.service.UpdateProductStockService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class FranchiseHandler {

    private final CreateFranchiseService createFranchiseService;
    private final AddBranchToFranchiseService addBranchToFranchiseService;
    private final AddProductToBranchService addProductToBranchService;
    private final UpdateProductStockService updateProductStockService;
    private final DeleteProductFromBranchService deleteProductFromBranchService;
    private final GetTopStockProductsByFranchiseService getTopStockProductsByFranchiseService;

    public Mono<ServerResponse> createFranchise(ServerRequest request) {
        return request.bodyToMono(CreateFranchiseRequest.class)
                .flatMap(req -> createFranchiseService.create(req.name()))
                .flatMap(franchise ->
                        ServerResponse
                                .ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(new FranchiseResponse(
                                        franchise.getId(),
                                        franchise.getName()
                                )));
    }

    public Mono<ServerResponse> addBranch(ServerRequest request) {
        String franchiseId = request.pathVariable("franchiseId");


        return request.bodyToMono(CreateBranchRequest.class)
                .flatMap(req ->
                        addBranchToFranchiseService.addBranch(franchiseId, req.name())
                )
                .then(ServerResponse.status(HttpStatus.CREATED).build());
    }

    public Mono<ServerResponse> addProduct(ServerRequest request) {
        String franchiseId = request.pathVariable("franchiseId");
        String branchName = request.pathVariable("branchName");

        return request.bodyToMono(CreateProductRequest.class)
                .flatMap(req ->
                        addProductToBranchService.addProduct(
                                franchiseId,
                                branchName,
                                req.name(),
                                req.stock()
                        )
                )
                .then(ServerResponse.status(HttpStatus.CREATED).build());
    }

    public Mono<ServerResponse> updateProductStock(ServerRequest request) {
        String franchiseId = request.pathVariable("franchiseId");
        String branchName = request.pathVariable("branchName");
        String productName = request.pathVariable("productName");


        return request.bodyToMono(UpdateStockRequest.class)
                .flatMap(req ->
                        updateProductStockService.updateStock(
                                franchiseId,
                                branchName,
                                productName,
                                req.stock()
                        )
                )
                .then(ServerResponse.ok().build());
    }

    public Mono<ServerResponse> deleteProduct(ServerRequest request) {
        String franchiseId = request.pathVariable("franchiseId");
        String branchName = request.pathVariable("branchName");
        String productName = request.pathVariable("productName");


        return deleteProductFromBranchService
                .delete(franchiseId, branchName, productName)
                .then(ServerResponse.noContent().build());
    }

    public Mono<ServerResponse> getTopStockProductsByFranchise(ServerRequest request) {
        String franchiseId = request.pathVariable("franchiseId");


        return getTopStockProductsByFranchiseService
                .getTopStockByFranchise(franchiseId)
                .flatMap(list ->
                        list.isEmpty()
                                ? ServerResponse.notFound().build()
                                : ServerResponse.ok().bodyValue(list)
                );
    }
}
