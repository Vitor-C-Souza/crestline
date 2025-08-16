package br.com.grooworks.crestline.domain.dto;

import com.braintreegateway.Customer;

public record CustomerResDto(
        String id,
        String firstName,
        String email,
        String token
) {
    public CustomerResDto(Customer customer, String paymentToken) {
        this(
                customer.getId(),
                customer.getFirstName(),
                customer.getEmail(),
                paymentToken
        );
    }
}
