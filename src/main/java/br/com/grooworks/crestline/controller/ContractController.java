package br.com.grooworks.crestline.controller;

import br.com.grooworks.crestline.domain.dto.SendContractDto;
import br.com.grooworks.crestline.domain.model.Contract;
import br.com.grooworks.crestline.domain.service.ContractService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/contract")
@CrossOrigin(origins = "*")
@SecurityRequirement(name = "bearerAuth")
public class ContractController {

    @Autowired
    private ContractService service;

    @Operation(
            summary = "Buscar contrato pelo ID",
            tags = {"Contract"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Contrato encontrado",
                            content = @Content(schema = @Schema(implementation = Contract.class))),
                    @ApiResponse(responseCode = "404", description = "Contrato não encontrado")
            }
    )
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<Contract> get(@PathVariable("id") String id) {
        return ResponseEntity.ok(service.get(id));
    }

    @Operation(
            summary = "Listar todos os contratos",
            tags = {"Contract"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de contratos",
                            content = @Content(schema = @Schema(implementation = Contract.class)))
            }
    )
    @GetMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<List<Contract>> list() {
        return ResponseEntity.ok(service.list());
    }

    @Operation(
            summary = "Enviar um novo contrato",
            tags = {"Contract"},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados do contrato a ser enviado",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = SendContractDto.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "title": "Contrato de Venda",
                                              "description": "Contrato de venda de imóvel",
                                              "signerName": "Vitor Souza",
                                              "signerEmail": "vitor@email.com",
                                              "base64Pdf": "<base64-do-pdf>"
                                            }"""
                            )
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Contrato criado e enviado",
                            content = @Content(schema = @Schema(implementation = Contract.class))),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos")
            }
    )
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @PostMapping("/send")
    public ResponseEntity<Contract> send(
            @RequestBody @Valid SendContractDto dto,
            UriComponentsBuilder uriComponentsBuilder) {

        Contract contract = service.createAndSend(dto);
        URI uri = uriComponentsBuilder.path("/contract/{id}").buildAndExpand(contract.getId()).toUri();
        return ResponseEntity.created(uri).body(contract);
    }

    @Operation(
            summary = "Download do contrato em PDF",
            tags = {"Contract"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "PDF do contrato retornado",
                            content = @Content(mediaType = "application/pdf"))
            }
    )
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/{id}/download")
    public ResponseEntity<byte[]> download(@PathVariable("id") String id) {
        byte[] pdf = service.download(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=contract.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    @Operation(
            summary = "Obter documento do contrato em Base64",
            tags = {"Contract"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Documento do contrato em Base64 retornado",
                            content = @Content(schema = @Schema(implementation = Map.class)))
            }
    )
    @GetMapping("/{id}/document")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<Map<String, Object>> getDoc(@PathVariable String id) {
        byte[] pdf = service.download(id);
        return ResponseEntity.ok(Map.of("contractId", id, "base64", Base64.getEncoder().encodeToString(pdf)));
    }

    @Operation(
            summary = "Atualizar status do contrato",
            tags = {"Contract"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Status do contrato atualizado",
                            content = @Content(schema = @Schema(implementation = Contract.class)))
            }
    )
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @PatchMapping("/{id}/status")
    public Contract updateStatus(@PathVariable("id") String id) {
        return service.checkStatus(id);
    }

    @Operation(
            summary = "Reenviar contrato",
            tags = {"Contract"},
            responses = {
                    @ApiResponse(responseCode = "204", description = "Contrato reenviado com sucesso")
            }
    )
    @PostMapping("/{id}/resend")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Void> resend(@PathVariable("id") String id) {
        service.resend(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Deletar contrato",
            tags = {"Contract"},
            responses = {
                    @ApiResponse(responseCode = "204", description = "Contrato deletado com sucesso")
            }
    )
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable("id") String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
