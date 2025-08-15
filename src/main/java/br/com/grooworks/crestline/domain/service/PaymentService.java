package br.com.grooworks.crestline.domain.service;

import br.com.grooworks.crestline.domain.dto.CreateCustomerDto;
import com.braintreegateway.CreditCard;
import com.braintreegateway.Customer;
import com.braintreegateway.Result;
import com.braintreegateway.Transaction;

public interface PaymentService {
    CreditCard getCard(String token) throws Exception;

    Result<Transaction> pay(String token, String amount);

    Customer saveCardAndCustomer(CreateCustomerDto dto);

    Result<CreditCard> updateCard(String token, String expirationDate, String cvv);

    void deleteCreditCard(String token);
}
