package br.com.grooworks.crestline.controller;

import br.com.grooworks.crestline.domain.dto.*;
import br.com.grooworks.crestline.domain.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pagamento")
@CrossOrigin(origins = "*")
public class PaymentController {

    @Autowired
    private PaymentService service;

    @Operation(
            summary = "Buscar cartão pelo token de pagamento",
            tags = {"Pagamento"},
            description = "Busca as informações de um cartão através do token informado",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Cartão encontrado",
                            content = @Content(schema = @Schema(implementation = SaveCardResponseDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Cartão não encontrado")
            }
    )
    @SneakyThrows
    @GetMapping("/{token}")
    public ResponseEntity<SaveCardResponseDTO> getCard(@PathVariable("token") String token) {
        SaveCardResponseDTO card = service.getCard(token);
        return ResponseEntity.ok(card);
    }

    @Operation(
            summary = "Realizar pagamento",
            tags = {"Pagamento"},
            description = "Efetua um pagamento utilizando o token do cartão e o valor especificado",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Pagamento realizado",
                            content = @Content(schema = @Schema(implementation = PaymentResDto.class))),
                    @ApiResponse(responseCode = "400", description = "Erro no pagamento")
            }
    )
    @PostMapping("/pay")
    public ResponseEntity<PaymentResDto> pay(@RequestParam("token") String token, @RequestParam("amount") String amount) {
        PaymentResDto pay = service.pay(token, amount);
        return ResponseEntity.ok(pay);
    }

    @Operation(
            summary = "Salvar cartão e cliente",
            tags = {"Pagamento"},
            description = "Cadastra um novo cliente junto com as informações de cartão",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Cliente e cartão salvos",
                            content = @Content(schema = @Schema(implementation = CustomerResDto.class))),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos")
            }
    )
    @PostMapping("/save")
    public ResponseEntity<CustomerResDto> saveCardAndCustomer(@RequestBody @Valid @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Dados do cliente e do cartão",
            required = true,
            content = @Content(
                    schema = @Schema(implementation = CreateCustomerDto.class),
                    examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                            value = "{ \"name\": \"Vitor Souza\", \"email\": \"vitor@email.com\", \"cardNumber\": \"4111111111111111\", \"expirationDate\": \"12/25\", \"cvv\": \"123\" }"
                    )
            )) CreateCustomerDto dto) {
        CustomerResDto customer = service.saveCardAndCustomer(dto);
        return ResponseEntity.ok(customer);
    }

    @Operation(
            summary = "Atualizar cartão",
            tags = {"Pagamento"},
            description = "Atualiza os dados de um cartão (data de expiração e CVV) a partir do token informado",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Cartão atualizado",
                            content = @Content(schema = @Schema(implementation = CardDto.class))),
                    @ApiResponse(responseCode = "404", description = "Cartão não encontrado")
            }
    )
    @PutMapping("/{token}")
    public ResponseEntity<CardDto> updateCard(@PathVariable("token") String token,
                                              @RequestParam("expirationDate") String expirationDate,
                                              @RequestParam("cvv") String cvv) {
        CardDto cardDto = service.updateCard(token, expirationDate, cvv);
        return ResponseEntity.ok(cardDto);
    }

    @Operation(
            summary = "Deletar cartão",
            tags = {"Pagamento"},
            description = "Remove permanentemente um cartão cadastrado a partir do token informado",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Cartão deletado"),
                    @ApiResponse(responseCode = "404", description = "Cartão não encontrado")
            }
    )
    @DeleteMapping("/{token}")
    public ResponseEntity<Void> deleteCard(@PathVariable("token") String token) {
        service.deleteCreditCard(token);
        return ResponseEntity.noContent().build();
    }
}
