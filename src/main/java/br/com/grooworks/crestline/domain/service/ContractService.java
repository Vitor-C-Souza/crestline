package br.com.grooworks.crestline.domain.service;

import br.com.grooworks.crestline.domain.dto.ContractResponseDto;
import br.com.grooworks.crestline.domain.dto.SendContractDto;

import java.util.List;

public interface ContractService {
    ContractResponseDto createAndSend(SendContractDto dto);

    ContractResponseDto getContract(String id);

    List<ContractResponseDto> list();

    byte[] download(String id);

    ContractResponseDto checkStatus(String id);

    void resend(String id);

    void delete(String id);
}
