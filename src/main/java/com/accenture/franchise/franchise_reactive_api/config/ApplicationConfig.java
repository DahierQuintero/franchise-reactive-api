package com.accenture.franchise.franchise_reactive_api.config;

import com.accenture.franchise.franchise_reactive_api.application.service.CreateFranchiseService;
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
}
