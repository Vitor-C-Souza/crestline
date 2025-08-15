package br.com.grooworks.crestline.controller;

import br.com.grooworks.crestline.domain.dto.CreateCustomerDto;
import br.com.grooworks.crestline.domain.service.PaymentService;
import com.braintreegateway.CreditCard;
import com.braintreegateway.Customer;
import com.braintreegateway.Result;
import com.braintreegateway.Transaction;
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
    public ResponseEntity<Customer> saveCardAndCustomer(@RequestBody @Valid CreateCustomerDto dto) {
        Customer customer = service.saveCardAndCustomer(dto);
        return ResponseEntity.ok(customer);
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
