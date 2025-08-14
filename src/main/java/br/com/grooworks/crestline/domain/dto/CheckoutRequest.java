package br.com.grooworks.crestline.domain.dto;

public record CheckoutRequest(
        String paymentMethodNonce,
        String amount,
        String firstName,
        String email,
        String cpf
) {
}
