package br.com.grooworks.crestline.domain.dto;

import br.com.grooworks.crestline.domain.model.Perfil;
import br.com.grooworks.crestline.domain.model.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

public record UpdateAuthResponse(
        String id,
        String username,
        String password,
        String email,
        @JsonIgnoreProperties("user")
        Perfil perfil
) {
    public UpdateAuthResponse(User user) {
        this(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.getEmail(),
                user.getPerfil() != null
                        ? user.getPerfil() : null
        );
    }
}
