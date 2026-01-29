package com.accenture.franchise.franchise_reactive_api.api.router;

import com.accenture.franchise.franchise_reactive_api.api.handler.FranchiseHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class FranchiseRouter {

    @Bean
    public RouterFunction<ServerResponse> franchiseRoutes(FranchiseHandler handler) {
        return RouterFunctions
                .route()
                .POST("/franchises", handler::createFranchise)
                .POST("/franchises/{franchiseId}/branches", handler::addBranch)
                .POST("/franchises/{franchiseId}/branches/{branchName}/products", handler::addProduct)
                .PUT("/franchises/{franchiseId}/branches/{branchName}/products/{productName}/stock", handler::updateProductStock)
                .DELETE("/franchises/{franchiseId}/branches/{branchName}/products/{productName}", handler::deleteProduct)
                .GET("/franchises/{franchiseId}/branches/products/top-stock", handler::getTopStockProductsByFranchise)
                .build();
    }
}
