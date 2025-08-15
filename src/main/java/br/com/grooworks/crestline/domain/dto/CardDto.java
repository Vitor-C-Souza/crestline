package br.com.grooworks.crestline.domain.dto;

import com.braintreegateway.CreditCard;

public record CardDto(
        String last4,
        String cardType,
        String imageUrl,
        String expirationMonth,
        String expirationYear,
        boolean isDefault
) {
    public CardDto(CreditCard creditCard) {
        this(
                creditCard.getLast4(),
                creditCard.getCardType(),
                creditCard.getImageUrl(),
                creditCard.getExpirationMonth(),
                creditCard.getExpirationYear(),
                creditCard.isDefault()
        );
    }
}
