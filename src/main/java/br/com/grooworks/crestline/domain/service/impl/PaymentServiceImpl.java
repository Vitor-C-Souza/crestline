package br.com.grooworks.crestline.domain.service.impl;


import br.com.grooworks.crestline.domain.service.PaymentService;
import br.com.grooworks.crestline.infra.exception.PaymentException;
import br.com.grooworks.crestline.infra.exception.SaveCardException;
import br.com.grooworks.crestline.infra.exception.UpdateCardException;
import br.com.grooworks.crestline.infra.exception.deleteCreditCardException;
import com.braintreegateway.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PaymentServiceImpl implements PaymentService {
    private final BraintreeGateway gateway;

    public PaymentServiceImpl(BraintreeGateway gateway) {
        this.gateway = gateway;
    }

    @Override
    public CreditCard getCard(String token) {
        try {
            return gateway.creditCard().find(token);
        } catch (Exception e) {
            throw new RuntimeException("Cartão não encontrado: " + e.getMessage());
        }
    }

    @Override
    public Result<Transaction> pay(String token, String amount) {
        try {
            TransactionRequest request = new TransactionRequest()
                    .amount(new BigDecimal(amount))
                    .paymentMethodToken(token)
                    .options()
                    .submitForSettlement(true)
                    .done();

            Result<Transaction> result = gateway.transaction().sale(request);

            if (!result.isSuccess()) {
                throw new PaymentException("Pagamento recusado: " + result.getMessage());
            }

            return result;
        } catch (NumberFormatException e) {
            throw new PaymentException("Valor inválido para pagamento: " + amount);

        } catch (PaymentException e) {
            throw e;

        } catch (Exception e) {
            throw new PaymentException("Erro interno ao processar pagamento: " + e.getMessage());
        }
    }

    @Override
    public Result<? extends PaymentMethod> saveCard(String paymentMethodNonce, String customerId) {
        try {
            PaymentMethodRequest request = new PaymentMethodRequest()
                    .customerId(customerId)
                    .paymentMethodNonce(paymentMethodNonce)
                    .options()
                    .makeDefault(true)
                    .verifyCard(true)
                    .done();

            Result<? extends PaymentMethod> result = gateway.paymentMethod().create(request);

            if (!result.isSuccess()) {
                throw new SaveCardException(result.getMessage());
            }

            return result;
        } catch (IllegalArgumentException e) {
            throw new SaveCardException("Parâmetros inválidos para salvar o cartão");

        } catch (SaveCardException e) {
            throw e;

        } catch (Exception e) {
            throw new SaveCardException("Erro interno ao salvar cartão: " + e.getMessage());
        }
    }

    @Override
    public Result<CreditCard> updateCard(String token, String expirationDate, String cvv) {
        try {
            CreditCardRequest request = new CreditCardRequest()
                    .expirationDate(expirationDate)
                    .cvv(cvv);

            Result<CreditCard> result = gateway.creditCard().update(token, request);

            if (!result.isSuccess()) {
                throw new UpdateCardException(result.getMessage());
            }

            return result;
        } catch (IllegalArgumentException e) {
            throw new SaveCardException("Parâmetros inválidos para salvar o cartão");

        } catch (UpdateCardException e) {
            throw e;
        } catch (Exception e) {
            throw new SaveCardException("Erro interno ao atualizar o cartão: " + e.getMessage());
        }
    }

    @Override
    public void deleteCreditCard(String token) {
        try {
            Result<CreditCard> result = gateway.creditCard().delete(token);

            if (!result.isSuccess()) {
                throw new deleteCreditCardException(result.getMessage());
            }
        } catch (IllegalArgumentException e) {
            throw new SaveCardException("Parâmetros inválidos para deletar o cartão");

        } catch (deleteCreditCardException e) {
            throw e;
        } catch (Exception e) {
            throw new deleteCreditCardException("Erro ao remover cartão: " + e.getMessage());
        }
    }
}
