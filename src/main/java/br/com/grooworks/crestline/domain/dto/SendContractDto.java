package br.com.grooworks.crestline.domain.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SendContractDto(
        @NotBlank String title,
        String description,
        @NotBlank String signerName,
        @Email @NotBlank String signerEmail,
        @NotNull String base64Pdf
) {
}
