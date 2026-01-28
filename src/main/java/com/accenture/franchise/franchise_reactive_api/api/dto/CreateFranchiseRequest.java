package com.accenture.franchise.franchise_reactive_api.api.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateFranchiseRequest(
        @NotBlank String name
) {}
