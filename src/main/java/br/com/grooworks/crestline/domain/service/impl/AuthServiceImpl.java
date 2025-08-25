package br.com.grooworks.crestline.domain.service.impl;

import br.com.grooworks.crestline.domain.dto.AuthRequest;
import br.com.grooworks.crestline.domain.dto.AuthResponse;
import br.com.grooworks.crestline.domain.dto.UpdateAuthRequest;
import br.com.grooworks.crestline.domain.dto.UpdateAuthResponse;
import br.com.grooworks.crestline.domain.model.Role;
import br.com.grooworks.crestline.domain.model.User;
import br.com.grooworks.crestline.domain.repository.UserRepository;
import br.com.grooworks.crestline.domain.service.AuthService;
import br.com.grooworks.crestline.infra.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthenticationManager authManager;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;

    @Override
    @Transactional
    public AuthResponse login(AuthRequest req) {
        authManager.authenticate(new UsernamePasswordAuthenticationToken(req.username(), req.password()));
        User user = userRepository.findByUsername(req.username()).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        String token = jwtService.generateToken(user);
        return new AuthResponse(token);
    }

    @Override
    @Transactional
    public User register(User user) {
        System.out.println(user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        if (user.getRole() == null) {
            user.setRole(Role.USER);
        }

        return userRepository.save(user);
    }

    @Override
    @Transactional
    public UpdateAuthResponse updateUser(String userId, UpdateAuthRequest req) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("Usuário não encontrado com esse id " + userId));

        if (req.username() != null && !req.username().isBlank()) {
            user.setUsername(req.username());
        }
        if (req.email() != null && !req.email().isBlank()) {
            user.setEmail(req.email());
        }
        if (req.password() != null && !req.password().isBlank()) {
            user.setPassword(passwordEncoder.encode(req.password()));
        }

        return new UpdateAuthResponse(userRepository.save(user));
    }
}
