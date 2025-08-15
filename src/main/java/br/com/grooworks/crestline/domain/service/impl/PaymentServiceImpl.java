package br.com.grooworks.crestline.domain.service.impl;


import br.com.grooworks.crestline.domain.dto.CreateCustomerDto;
import br.com.grooworks.crestline.domain.model.CustomerEntity;
import br.com.grooworks.crestline.domain.repository.CustomerEntityRepository;
import br.com.grooworks.crestline.domain.service.PaymentService;
import br.com.grooworks.crestline.infra.exception.PaymentException;
import br.com.grooworks.crestline.infra.exception.SaveCardException;
import br.com.grooworks.crestline.infra.exception.UpdateCardException;
import br.com.grooworks.crestline.infra.exception.deleteCreditCardException;
import com.braintreegateway.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PaymentServiceImpl implements PaymentService {
    private final BraintreeGateway gateway;

    public PaymentServiceImpl(BraintreeGateway gateway) {
        this.gateway = gateway;
    }

    @Autowired
    private CustomerEntityRepository repository;

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
    public Customer saveCardAndCustomer(CreateCustomerDto dto) {
        try {
            CustomerRequest request = new CustomerRequest()
                    .firstName(dto.firstName())
                    .email(dto.email())
                    .customField("cpf", dto.cpf())
                    .paymentMethodNonce(dto.paymentMethodNonce());

            Result<Customer> result = gateway.customer().create(request);

            if (!result.isSuccess()) {
                throw new SaveCardException("Erro ao criar cliente: " + result.getMessage());
            }

            Customer customer = result.getTarget();

            String paymentToken = customer.getPaymentMethods().get(0).getToken();

            repository.save(
                    new CustomerEntity(customer.getId(), dto.cpf(), paymentToken, 1L)
            );

            return customer;
        } catch (Exception e) {
            throw new SaveCardException("Erro ao criar cliente e salvar cartão: " + e.getMessage());
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
