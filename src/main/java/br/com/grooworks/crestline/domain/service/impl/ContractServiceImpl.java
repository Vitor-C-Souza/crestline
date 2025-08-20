package br.com.grooworks.crestline.domain.service.impl;

import br.com.grooworks.crestline.domain.dto.SendContractDto;
import br.com.grooworks.crestline.domain.model.Contract;
import br.com.grooworks.crestline.domain.repository.ContractRepository;
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

import java.time.Instant;
import java.util.Base64;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ContractServiceImpl implements ContractService {

    @Autowired
    private ContractRepository repository;

    @Autowired
    private DocuSignService docuSignService;

    @Override
    public Contract createAndSend(SendContractDto dto) {
        byte[] pdf = Base64.getDecoder().decode(dto.base64Pdf());
        String envelopeId;
        try {
            envelopeId = docuSignService.sendEnvelope(dto, pdf, dto.title() + ".pdf", "pdf");
        } catch (ApiException e) {
            throw new DocuSignSendException("Erro ao enviar contrato", e);
        }
        Contract contract = new Contract(dto, envelopeId);
        return repository.save(contract);
    }

    @Override
    public Contract get(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new ContractNotFoundException("Contrato não encontrado com id: " + id));
    }

    @Override
    public List<Contract> list() {
        return repository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
    }

    @SneakyThrows
    @Override
    public byte[] download(String id) {
        Contract contract = get(id);
        return docuSignService.getDocumentPdf(contract.getEnvelopeId(), contract.getDocumentId());
    }

    @SneakyThrows
    @Override
    public Contract checkStatus(String id) {
        Contract contract = get(id);
        String status = docuSignService.getEnvelope(contract.getEnvelopeId()).getStatus();
        contract.setStatus(status);
        contract.setUpdatedAt(Instant.now());
        return repository.save(contract);
    }

    @SneakyThrows
    @Override
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
    public void delete(String id) {
        Contract contract = get(id);
        try {
            docuSignService.voidEnvelope(contract.getEnvelopeId(), "Deletado pelo sistema");
            repository.deleteById(id);
        } catch (ApiException e) {
            throw new DocuSignDeleteException("Erro ao deletar contrato", e);
        }
    }
}
