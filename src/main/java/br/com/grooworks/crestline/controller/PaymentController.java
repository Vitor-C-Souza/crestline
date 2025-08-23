package br.com.grooworks.crestline.controller;

import br.com.grooworks.crestline.domain.dto.*;
import br.com.grooworks.crestline.domain.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
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

    @Operation(summary = "Buscar cartão pelo token de pagamento", tags = {"Pagamento"}, description = "Busca as informações de um cartão através do token informado", responses = {@ApiResponse(responseCode = "200", description = "Cartão encontrado", content = @Content(schema = @Schema(implementation = SaveCardResponseDTO.class))), @ApiResponse(responseCode = "404", description = "Cartão não encontrado")})
    @SneakyThrows
    @GetMapping("/{token}")
    public ResponseEntity<SaveCardResponseDTO> getCard(@PathVariable("token") String token) {
        SaveCardResponseDTO card = service.getCard(token);
        return ResponseEntity.ok(card);
    }


    @Operation(summary = "Realizar pagamento", tags = {"Pagamento"}, description = "Efetua um pagamento utilizando o token do cartão e o valor especificado", responses = {@ApiResponse(responseCode = "200", description = "Pagamento realizado", content = @Content(schema = @Schema(implementation = PaymentResDto.class))), @ApiResponse(responseCode = "400", description = "Erro no pagamento")})
    @PostMapping("/pay")
    public ResponseEntity<PaymentResDto> pay(@Parameter(description = "Token do cartão") @RequestParam("token") String token, @Parameter(description = "Valor da transação. Ex: 100.00") @RequestParam("amount") String amount) {
        PaymentResDto pay = service.pay(token, amount);
        return ResponseEntity.ok(pay);
    }

    @Operation(summary = "Salvar cartão e cliente", tags = {"Pagamento"}, description = "Cadastra um novo cliente junto com as informações de cartão", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Dados do cliente e do cartão", required = true, content = @Content(schema = @Schema(implementation = CreateCustomerDto.class), examples = @ExampleObject(value = """
            {
              "firstName": "Vitor Souza",
              "email": "vitor@email.com",
              "cpf": "12345678901",
              "paymentMethodNonce": "fake-valid-nonce",
              "userId": 1
            }"""))), responses = {@ApiResponse(responseCode = "200", description = "Cliente e cartão salvos com sucesso", content = @Content(schema = @Schema(implementation = CustomerResDto.class))), @ApiResponse(responseCode = "400", description = "Dados inválidos")})
    @PostMapping("/save")
    public ResponseEntity<CustomerResDto> saveCardAndCustomer(@RequestBody @Valid CreateCustomerDto dto) {
        CustomerResDto customer = service.saveCardAndCustomer(dto);
        return ResponseEntity.ok(customer);
    }

    @Operation(summary = "Atualizar cartão", tags = {"Pagamento"}, description = "Atualiza os dados de um cartão (data de expiração e CVV) a partir do token informado", responses = {@ApiResponse(responseCode = "200", description = "Cartão atualizado", content = @Content(schema = @Schema(implementation = CardDto.class))), @ApiResponse(responseCode = "404", description = "Cartão não encontrado")})
    @PutMapping("/{token}")
    public ResponseEntity<CardDto> updateCard(@PathVariable("token") String token, @Parameter(description = "Nova data de expiração (MM/YY)", example = "12/30") @RequestParam("expirationDate") String expirationDate, @Parameter(description = "Novo código CVV", example = "321") @RequestParam("cvv") String cvv) {
        CardDto cardDto = service.updateCard(token, expirationDate, cvv);
        return ResponseEntity.ok(cardDto);
    }

    @Operation(summary = "Deletar cartão", tags = {"Pagamento"}, description = "Remove permanentemente um cartão cadastrado a partir do token informado", responses = {@ApiResponse(responseCode = "204", description = "Cartão deletado"), @ApiResponse(responseCode = "404", description = "Cartão não encontrado")})
    @DeleteMapping("/{token}")
    public ResponseEntity<Void> deleteCard(@PathVariable("token") String token) {
        service.deleteCreditCard(token);
        return ResponseEntity.noContent().build();
    }
}
