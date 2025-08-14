package br.com.grooworks.crestline.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "usuario_pagamento")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioPagamento {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    private Long usuarioId;
    private String customerIdBraintree;
    private String paymentToken;
}
