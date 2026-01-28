package com.accenture.franchise.franchise_reactive_api.api.handler;

import com.accenture.franchise.franchise_reactive_api.api.dto.CreateBranchRequest;
import com.accenture.franchise.franchise_reactive_api.api.dto.CreateFranchiseRequest;
import com.accenture.franchise.franchise_reactive_api.api.dto.FranchiseResponse;
import com.accenture.franchise.franchise_reactive_api.application.service.AddBranchToFranchiseService;
import com.accenture.franchise.franchise_reactive_api.application.service.CreateFranchiseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class FranchiseHandler {

    private final CreateFranchiseService createFranchiseService;
    private final AddBranchToFranchiseService addBranchToFranchiseService;

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
                .then(ServerResponse.noContent().build());
    }
}
