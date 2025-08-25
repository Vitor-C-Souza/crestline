package br.com.grooworks.crestline.domain.dto;

public record UpdateAuthRequest(
        String username,
        String email,
        String password
) {
}
