package br.com.grooworks.crestline.domain.service;

import br.com.grooworks.crestline.domain.dto.AuthRequest;
import br.com.grooworks.crestline.domain.dto.AuthResponse;
import br.com.grooworks.crestline.domain.dto.UpdateAuthRequest;
import br.com.grooworks.crestline.domain.dto.UpdateAuthResponse;
import br.com.grooworks.crestline.domain.model.User;

public interface AuthService {

    AuthResponse login(AuthRequest req);

    User register(User user);

    UpdateAuthResponse updateUser(String userId, UpdateAuthRequest req);
}
