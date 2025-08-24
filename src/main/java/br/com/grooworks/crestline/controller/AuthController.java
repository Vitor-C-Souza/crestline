package br.com.grooworks.crestline.controller;

import br.com.grooworks.crestline.domain.dto.AuthRequest;
import br.com.grooworks.crestline.domain.dto.AuthResponse;
import br.com.grooworks.crestline.domain.dto.RegisterRequest;
import br.com.grooworks.crestline.domain.model.User;
import br.com.grooworks.crestline.domain.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    @Operation(
            summary = "Registrar novo usuário",
            tags = {"Autenticação"},
            description = "Cria um novo usuário no sistema",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados do usuário a ser registrado",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = RegisterRequest.class),
                            examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                                    value = "{ \"username\": \"vii\", \"password\": \"123456\", \"email\": \"vii@example.com\", \"role\": \"ADMIN\" }"
                            )
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Usuário registrado com sucesso"),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos")
            }
    )
    public ResponseEntity<User> register(@RequestBody RegisterRequest req) {
        User user = new User();
        user.setUsername(req.username());
        user.setPassword(req.password());
        user.setEmail(req.email());
        user.setRole(req.role());
        User registered = authService.register(user);
        return ResponseEntity.ok(registered);
    }

    @Operation(
            summary = "Login",
            tags = {"Autenticação"},
            description = "Realiza login e retorna o token JWT",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Login realizado com sucesso",
                            content = @Content(schema = @Schema(implementation = AuthResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Credenciais inválidas")
            }
    )
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @org.springframework.web.bind.annotation.RequestBody AuthRequest req
    ) {
        return ResponseEntity.ok(authService.login(req));
    }
}
