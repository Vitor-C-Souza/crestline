package br.com.grooworks.crestline.domain.service.impl;

import br.com.grooworks.crestline.domain.dto.SendContractDto;
import br.com.grooworks.crestline.domain.model.Contract;
import br.com.grooworks.crestline.domain.repository.ContractRepository;
import br.com.grooworks.crestline.domain.service.ContractService;
import br.com.grooworks.crestline.domain.service.DocuSignService;
import com.docusign.esign.client.ApiException;
import lombok.SneakyThrows;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Base64;
import java.util.List;

@Service
public class ContractServiceImpl implements ContractService {

    private ContractRepository repository;
    private DocuSignService docuSignService;

    @Override
    public Contract createAndSend(SendContractDto dto) {
        byte[] pdf = Base64.getDecoder().decode(dto.base64Pdf());
        String envelopeId = null;
        try {
            envelopeId = docuSignService.sendEnvelope(dto, pdf, dto.title() + ".pdf", "pdf");
        } catch (ApiException e) {
            throw new RuntimeException("Erro ao enviar contrato: " + e.getMessage(), e);
        }

        Contract contract = new Contract(dto, envelopeId);


        return repository.save(contract);
    }

    @Override
    public Contract get(String id) {
        return repository.findById(id).orElseThrow();
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
    public Contract checkStatus(String id)  {
        Contract contract = get(id);
        String status = docuSignService.getEnvelope(contract.getEnvelopeId()).getStatus();
        contract.setStatus(status);
        contract.setUpdatedAt(Instant.now());
        return repository.save(contract);
    }

    @SneakyThrows
    @Override
    public void resend(String id)  {
        Contract contract = get(id);
        docuSignService.resendEnvelope(contract.getEnvelopeId());
    }

    @SneakyThrows
    @Override
    public void delete(String id)  {
        Contract contract = get(id);
        docuSignService.voidEnvelope(contract.getEnvelopeId(), "Deletado pelo sistema");
        repository.deleteById(id);
    }
}
