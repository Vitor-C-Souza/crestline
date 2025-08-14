package br.com.grooworks.crestline.domain.service;

import com.braintreegateway.CreditCard;
import com.braintreegateway.PaymentMethod;
import com.braintreegateway.Result;
import com.braintreegateway.Transaction;

public interface PaymentService {
    CreditCard getCard(String token) throws Exception;

    Result<Transaction> pay(String token, String amount);

    Result<? extends PaymentMethod> saveCard(String paymentMethodNonce, String customerId);

    Result<CreditCard> updateCard(String token, String expirationDate, String cvv);

    void deleteCreditCard(String token);
}
