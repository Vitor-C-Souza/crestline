package br.com.grooworks.crestline.domain.dto;

import com.braintreegateway.Transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentResDto(
        String transactionId,
        String status,
        BigDecimal amount,
        String currency,
        LocalDateTime createdAt
) {
    public PaymentResDto(Transaction transaction) {
        this(
                transaction.getId(),
                transaction.getStatus().toString(),
                transaction.getAmount(),
                transaction.getCurrencyIsoCode(),
                transaction.getCreatedAt().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime()
        );
    }
}
