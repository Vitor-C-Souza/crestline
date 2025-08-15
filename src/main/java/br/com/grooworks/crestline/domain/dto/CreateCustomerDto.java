package br.com.grooworks.crestline.domain.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateCustomerDto(
        @NotBlank String firstName,
        @NotBlank String email,
        @NotBlank String cpf,
        @NotBlank String paymentMethodNonce,
        Long userId
) {
}
