package br.com.grooworks.crestline.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "Costumer")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private Long usuarioId;
    private String customerIdBraintree;
    private String paymentToken;
    private String cpf;

    public CustomerEntity(String customerId, String cpf, String paymentToken, Long userId) {
        this.customerIdBraintree = customerId;
        this.cpf = cpf;
        this.paymentToken = paymentToken;
        this.usuarioId = userId;
    }
}
