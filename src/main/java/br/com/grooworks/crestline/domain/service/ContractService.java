package br.com.grooworks.crestline.domain.service;

import br.com.grooworks.crestline.domain.dto.SendContractDto;
import br.com.grooworks.crestline.domain.model.Contract;

import java.util.List;

public interface ContractService {
    Contract createAndSend(SendContractDto dto);

    Contract get(String id);

    List<Contract> list();

    byte[] download(String id);

    Contract checkStatus(String id);

    void resend(String id);

    void delete(String id);
}
