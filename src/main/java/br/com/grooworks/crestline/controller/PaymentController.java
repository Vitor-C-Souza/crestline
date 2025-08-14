package br.com.grooworks.crestline.controller;

import br.com.grooworks.crestline.domain.service.PaymentService;
import com.braintreegateway.CreditCard;
import com.braintreegateway.PaymentMethod;
import com.braintreegateway.Result;
import com.braintreegateway.Transaction;
import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class PaymentController {

    private PaymentService service;

    @SneakyThrows
    @GetMapping("/{token}")
    public ResponseEntity<?> getCard(@PathVariable String token) {
        CreditCard card = service.getCard(token);
        return ResponseEntity.ok(card);
    }

    @PostMapping("/pay")
    public ResponseEntity<?> pay(@RequestParam String token, @RequestParam String amount) {
        Result<Transaction> pay = service.pay(token, amount);
        return ResponseEntity.ok(pay.getTarget());
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveCard(@RequestParam String paymentMethodNonce, @RequestParam String customerId) {
        Result<? extends PaymentMethod> result = service.saveCard(paymentMethodNonce, customerId);
        return ResponseEntity.ok(result.getTarget());
    }

    @PutMapping("/{token}")
    public ResponseEntity<?> updateCard(@PathVariable String token,
                                        @RequestParam String expirationDate,
                                        @RequestParam String cvv) {
        Result<CreditCard> result = service.updateCard(token, expirationDate, cvv);
        return ResponseEntity.ok(result.getTarget());
    }

    @DeleteMapping("/{token}")
    public ResponseEntity<?> deleteCard(@PathVariable String token) {
        service.deleteCreditCard(token);
        return ResponseEntity.noContent().build();
    }
}
