package br.com.grooworks.crestline.domain.dto;

import com.braintreegateway.Customer;

public record SaveCardResponseDTO(
        String customerId,
        String firstName,
        String email,
        CardDto card
) {
    public SaveCardResponseDTO(CardDto cardDto, Customer customer) {
        this(
                customer.getId(),
                customer.getFirstName(),
                customer.getEmail(),
                cardDto
        );
    }
}
