package br.com.grooworks.crestline.controller;

import br.com.grooworks.crestline.domain.dto.*;
import br.com.grooworks.crestline.domain.service.PaymentService;
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

    @SneakyThrows
    @GetMapping("/{token}")
    public ResponseEntity<SaveCardResponseDTO> getCard(@PathVariable("token") String token) {
        SaveCardResponseDTO card = service.getCard(token);
        return ResponseEntity.ok(card);
    }

    @PostMapping("/pay")
    public ResponseEntity<PaymentResDto> pay(@RequestParam("token") String token, @RequestParam("amount") String amount) {
        PaymentResDto pay = service.pay(token, amount);
        return ResponseEntity.ok(pay);
    }

    @PostMapping("/save")
    public ResponseEntity<CustomerResDto> saveCardAndCustomer(@RequestBody @Valid CreateCustomerDto dto) {
        CustomerResDto customer = service.saveCardAndCustomer(dto);
        return ResponseEntity.ok(customer);
    }

    @PutMapping("/{token}")
    public ResponseEntity<CardDto> updateCard(@PathVariable("token") String token,
                                              @RequestParam("expirationDate") String expirationDate,
                                              @RequestParam("cvv") String cvv) {
        CardDto cardDto = service.updateCard(token, expirationDate, cvv);
        return ResponseEntity.ok(cardDto);
    }

    @DeleteMapping("/{token}")
    public ResponseEntity<Void> deleteCard(@PathVariable("token") String token) {
        service.deleteCreditCard(token);
        return ResponseEntity.noContent().build();
    }
}
