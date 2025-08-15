package br.com.grooworks.crestline.domain.dto;

import br.com.grooworks.crestline.domain.model.CustomerEntity;
import com.braintreegateway.Customer;
import jakarta.validation.constraints.NotBlank;

public record CreateCustomerDto(
        @NotBlank String firstName,
        @NotBlank String email,
        @NotBlank String cpf,
        @NotBlank String paymentMethodNonce,
        Long userId
) {

    public CreateCustomerDto(CustomerEntity customerEntity, Customer customer) {
        this(
                customer.getFirstName(),
                customer.getEmail(),
                customerEntity.getCpf(),
                customerEntity.getPaymentToken(),
                customerEntity.getUsuarioId()
        );
    }
}
