package br.com.grooworks.crestline.domain.dto;

import com.braintreegateway.Customer;

public record CustomerResDto(
        String id,
        String firstName,
        String email,
        String message
) {
    public CustomerResDto(Customer customer) {
        this(
                customer.getId(),
                customer.getFirstName(),
                customer.getEmail(),
                ""
        );
    }
}
