package br.com.grooworks.crestline.domain.dto;

import br.com.grooworks.crestline.domain.model.Role;

public record RegisterRequest(String username,
                              String password,
                              String email,
                              Role role) {
}
