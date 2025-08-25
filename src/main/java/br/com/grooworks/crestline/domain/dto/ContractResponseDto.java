package br.com.grooworks.crestline.domain.dto;

import br.com.grooworks.crestline.domain.model.Contract;

import java.time.Instant;

public record ContractResponseDto(
        String id,
        String title,
        String envelopeId,
        String status,
        Instant createdAt,
        String signerName,
        String signerEmail
) {

    public ContractResponseDto(Contract contract) {
        this(
                contract.getId(),
                contract.getTitle(),
                contract.getEnvelopeId(),
                contract.getStatus(),
                contract.getCreatedAt(),
                contract.getUser().getUsername(),
                contract.getUser().getEmail()
        );
    }
}
