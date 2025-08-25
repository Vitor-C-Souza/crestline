package br.com.grooworks.crestline.controller;

import br.com.grooworks.crestline.domain.dto.PerfilRequest;
import br.com.grooworks.crestline.domain.dto.PerfilResponse;
import br.com.grooworks.crestline.domain.service.PerfilService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/perfil")
@CrossOrigin(origins = "*")
@SecurityRequirement(name = "bearerAuth")
public class PerfilController {

    @Autowired
    private PerfilService service;

    @PostMapping
    @Operation(
            summary = "Criar perfil",
            tags = {"Perfil"},
            description = "Cria um novo perfil de usuário",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados do perfil a ser criado",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = PerfilRequest.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "firstName": "Vítor",
                                              "middleName": "C.",
                                              "lastName": "Souza",
                                              "telefone": "13996161719",
                                              "endereco": "Rua Exemplo, 123",
                                              "avatarBase64": "<base64_image_string>",
                                              "email": "vii@example.com"
                                            }"""
                            )
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Perfil criado com sucesso"),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos")
            }
    )
    public ResponseEntity<PerfilResponse> create(@RequestBody @Valid PerfilRequest dto, UriComponentsBuilder uriComponentsBuilder) {
        PerfilResponse response = service.create(dto);
        URI uri = uriComponentsBuilder.path("/perfil/{id}").buildAndExpand(response.id()).toUri();
        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(
            tags = {"Perfil"},
            summary = "Listar todos os perfis",
            description = "Retorna uma lista de perfis",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de perfis retornada",
                            content = @Content(schema = @Schema(implementation = PerfilResponse.class))),
                    @ApiResponse(responseCode = "403", description = "Acesso negado")
            }
    )
    public ResponseEntity<List<PerfilResponse>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @Operation(
            tags = {"Perfil"},
            summary = "Buscar perfil por ID",
            description = "Retorna os detalhes de um perfil específico",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Perfil encontrado",
                            content = @Content(schema = @Schema(implementation = PerfilResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Perfil não encontrado"),
                    @ApiResponse(responseCode = "403", description = "Acesso negado")
            }
    )
    public ResponseEntity<PerfilResponse> getById(@PathVariable("id") String id) {
        PerfilResponse response = service.getById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @Operation(
            summary = "Atualizar perfil",
            tags = {"Perfil"},
            description = "Atualiza os dados de um perfil existente",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados do perfil atualizados",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = PerfilRequest.class),
                            examples = @ExampleObject(
                                    value = "{ \"firstName\": \"Vítor\", \"middleName\": \"C.\", \"lastName\": \"Souza\", \"telefone\": \"13996161719\", \"endereco\": \"Rua Exemplo, 123\", \"avatarBase64\": \"...\", \"email\": \"vii@example.com\" }"
                            )
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Perfil atualizado com sucesso",
                            content = @Content(schema = @Schema(implementation = PerfilResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Perfil não encontrado"),
                    @ApiResponse(responseCode = "403", description = "Acesso negado")
            }
    )
    public ResponseEntity<PerfilResponse> update(@PathVariable("id") String id, @RequestBody @Valid PerfilRequest dto) {
        PerfilResponse response = service.update(id, dto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(
            tags = {"Perfil"},
            summary = "Excluir perfil",
            description = "Exclui um perfil existente",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Perfil excluído com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Perfil não encontrado"),
                    @ApiResponse(responseCode = "403", description = "Acesso negado")
            }
    )
    public ResponseEntity<Void> delete(@PathVariable("id") String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
