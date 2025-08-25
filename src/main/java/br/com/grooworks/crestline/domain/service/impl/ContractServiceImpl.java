package br.com.grooworks.crestline.domain.service.impl;

import br.com.grooworks.crestline.domain.dto.ContractResponseDto;
import br.com.grooworks.crestline.domain.dto.SendContractDto;
import br.com.grooworks.crestline.domain.model.Contract;
import br.com.grooworks.crestline.domain.model.User;
import br.com.grooworks.crestline.domain.repository.ContractRepository;
import br.com.grooworks.crestline.domain.repository.UserRepository;
import br.com.grooworks.crestline.domain.service.ContractService;
import br.com.grooworks.crestline.domain.service.DocuSignService;
import br.com.grooworks.crestline.infra.exception.ContractNotFoundException;
import br.com.grooworks.crestline.infra.exception.DocuSignDeleteException;
import br.com.grooworks.crestline.infra.exception.DocuSignResendException;
import br.com.grooworks.crestline.infra.exception.DocuSignSendException;
import com.docusign.esign.client.ApiException;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Base64;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class ContractServiceImpl implements ContractService {

    @Autowired
    private ContractRepository repository;

    @Autowired
    private DocuSignService docuSignService;

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public ContractResponseDto createAndSend(SendContractDto dto) {
        User user = userRepository.findByEmail(dto.signerEmail()).orElseThrow(() -> new NoSuchElementException("Usuario com este email não encontrado " + dto.signerEmail()));
        byte[] pdf = Base64.getDecoder().decode(dto.base64Pdf());
        String envelopeId;
        try {
            envelopeId = docuSignService.sendEnvelope(dto, pdf, dto.title() + ".pdf", "pdf");
        } catch (ApiException e) {
            throw new DocuSignSendException("Erro ao enviar contrato", e);
        }

        Contract contract = new Contract(dto, envelopeId, user);
        Contract save = repository.save(contract);
        return new ContractResponseDto(save);
    }

    @Override
    @Transactional(readOnly = true)
    public ContractResponseDto getContract(String id) {
        return new ContractResponseDto(get(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ContractResponseDto> list() {
        return repository.findAll(Sort.by(Sort.Direction.DESC, "createdAt")).stream()
                .map(ContractResponseDto::new)
                .collect(Collectors.toList());
    }

    @SneakyThrows
    @Override
    @Transactional
    public byte[] download(String id) {
        Contract contract = get(id);
        return docuSignService.getDocumentPdf(contract.getEnvelopeId(), contract.getDocumentId());
    }

    @SneakyThrows
    @Override
    @Transactional
    public ContractResponseDto checkStatus(String id) {
        Contract contract = get(id);
        String status = docuSignService.getEnvelope(contract.getEnvelopeId()).getStatus();
        contract.setStatus(status);
        contract.setUpdatedAt(Instant.now());
        Contract save = repository.save(contract);
        return new ContractResponseDto(save);
    }

    @SneakyThrows
    @Override
    @Transactional
    public void resend(String id) {
        Contract contract = get(id);
        try {
            docuSignService.resendEnvelope(contract.getEnvelopeId());
        } catch (ApiException e) {
            throw new DocuSignResendException("Erro ao reenviar contrato", e);
        }
    }

    @SneakyThrows
    @Override
    @Transactional
    public void delete(String id) {
        Contract contract = get(id);
        try {
            docuSignService.voidEnvelope(contract.getEnvelopeId(), "Deletado pelo sistema");
            repository.deleteById(id);
        } catch (ApiException e) {
            throw new DocuSignDeleteException("Erro ao deletar contrato", e);
        }
    }

    @Transactional(readOnly = true)
    private Contract get(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new ContractNotFoundException("Contrato não encontrado com id: " + id));
    }
}
