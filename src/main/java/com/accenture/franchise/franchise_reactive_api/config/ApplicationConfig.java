package com.accenture.franchise.franchise_reactive_api.config;

import com.accenture.franchise.franchise_reactive_api.application.service.AddBranchToFranchiseService;
import com.accenture.franchise.franchise_reactive_api.application.service.AddProductToBranchService;
import com.accenture.franchise.franchise_reactive_api.application.service.CreateFranchiseService;
import com.accenture.franchise.franchise_reactive_api.application.service.DeleteProductFromBranchService;
import com.accenture.franchise.franchise_reactive_api.application.service.GetTopStockProductsByFranchiseService;
import com.accenture.franchise.franchise_reactive_api.application.service.UpdateProductStockService;
import com.accenture.franchise.franchise_reactive_api.domain.repository.IFranchiseRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

    @Bean
    public CreateFranchiseService createFranchiseService(
            IFranchiseRepository franchiseRepository
    ) {
        return new CreateFranchiseService(franchiseRepository);
    }

    @Bean
    public AddBranchToFranchiseService addBranchToFranchiseService (
            IFranchiseRepository franchiseRepository
    ) {
        return new AddBranchToFranchiseService(franchiseRepository);
    }

    @Bean
    public AddProductToBranchService addProductToBranchService (
            IFranchiseRepository franchiseRepository
    ) {
        return new AddProductToBranchService(franchiseRepository);
    }

    @Bean
    public UpdateProductStockService updateProductStockService (
            IFranchiseRepository franchiseRepository
    ) {
        return new UpdateProductStockService(franchiseRepository);
    }

    @Bean
    public DeleteProductFromBranchService deleteProductFromBranchService (
            IFranchiseRepository franchiseRepository
    ) {
        return new DeleteProductFromBranchService(franchiseRepository);
    }

    @Bean
    public GetTopStockProductsByFranchiseService getTopStockProductService (
            IFranchiseRepository franchiseRepository
    ) {
        return new GetTopStockProductsByFranchiseService(franchiseRepository);
    }
}
