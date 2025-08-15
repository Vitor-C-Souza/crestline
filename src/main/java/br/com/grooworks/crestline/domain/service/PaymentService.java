package br.com.grooworks.crestline.domain.service;

import br.com.grooworks.crestline.domain.dto.*;

public interface PaymentService {
    SaveCardResponseDTO getCard(String token) throws Exception;

    PaymentResDto pay(String token, String amount);

    CustomerResDto getCustomerByUserId(Long id);

    CustomerResDto saveCardAndCustomer(CreateCustomerDto dto);

    CardDto updateCard(String token, String expirationDate, String cvv);

    void deleteCreditCard(String token);
}
