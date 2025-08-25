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


    private String customerIdBraintree;
    private String paymentToken;
    private String cpf;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    public CustomerEntity(String customerId, String cpf, String paymentToken, User user) {
        this.customerIdBraintree = customerId;
        this.cpf = cpf;
        this.paymentToken = paymentToken;
        this.user = user;
    }
}
