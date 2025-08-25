package br.com.grooworks.crestline.domain.dto;

import br.com.grooworks.crestline.domain.model.Perfil;

import java.util.Base64;

public record PerfilResponse(
        String id,
        String firstName,
        String middleName,
        String lastName,
        String telefone,
        String endereco,
        String avatarBase64, // devolve já convertido de byte[] -> Base64
        String userId,
        String username,
        String email
) {
    public PerfilResponse(Perfil perfil) {
        this(
                perfil.getId(),
                perfil.getFirstName(),
                perfil.getMiddleName(),
                perfil.getLastName(),
                perfil.getTelefone(),
                perfil.getEndereco(),
                perfil.getAvatarUrl() != null
                        ? Base64.getEncoder().encodeToString(perfil.getAvatarUrl())
                        : null,
                perfil.getUser() != null ? perfil.getUser().getId() : null,
                perfil.getUser() != null ? perfil.getUser().getUsername() : null,
                perfil.getUser() != null ? perfil.getUser().getEmail() : null
        );
    }
}
