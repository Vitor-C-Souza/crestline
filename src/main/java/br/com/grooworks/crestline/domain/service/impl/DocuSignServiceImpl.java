package br.com.grooworks.crestline.domain.service.impl;

import br.com.grooworks.crestline.domain.dto.SendContractDto;
import br.com.grooworks.crestline.domain.service.DocuSignAuthService;
import br.com.grooworks.crestline.domain.service.DocuSignService;
import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.client.ApiClient;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.List;

@Service
public class DocuSignServiceImpl implements DocuSignService {

    private final ApiClient apiClient;
    private final EnvelopesApi envelopesApi;
    private final String accountId;
    private final DocuSignAuthService authService;

    public DocuSignServiceImpl(
            @Value("${docusign.account-id}") String accountId,
            DocuSignAuthService authService
    ) {
        this.accountId = accountId;
        this.apiClient = new ApiClient("https://demo.docusign.net/restapi");
        this.authService = authService;
        this.envelopesApi = new EnvelopesApi(apiClient);
    }

    private void updateToken() {
        String token = authService.getAccessToken();
        apiClient.addDefaultHeader("Authorization", "Bearer " + token);
    }

    @Override
    public String sendEnvelope(SendContractDto dto, byte[] file, String fileName, String fileExt) throws ApiException {
        updateToken();

        EnvelopeDefinition envelope = new EnvelopeDefinition();
        envelope.setEmailSubject("Assinatura: " + dto.title());

        Document doc = new Document();
        doc.setDocumentBase64(Base64.getEncoder().encodeToString(file));
        doc.setName(fileName);
        doc.setFileExtension(fileExt);
        doc.setDocumentId("1");

        Signer signer = new Signer();
        signer.setEmail(dto.signerEmail());
        signer.setName(dto.signerName());
        signer.setRecipientId("1");

        SignHere signHere = new SignHere();
        signHere.setAnchorString("/sig/");
        signHere.setAnchorUnits("pixels");
        signHere.setAnchorXOffset("10");
        signHere.setAnchorYOffset("20");

        Tabs tabs = new Tabs();
        tabs.setSignHereTabs(List.of(signHere));
        signer.setTabs(tabs);

        Recipients recipients = new Recipients();
        recipients.setSigners(List.of(signer));

        envelope.setDocuments(List.of(doc));
        envelope.setRecipients(recipients);
        envelope.setStatus("sent");

        EnvelopeSummary result = envelopesApi.createEnvelope(accountId, envelope);
        return result.getEnvelopeId();
    }

    @Override
    public Envelope getEnvelope(String envelopeId) throws ApiException {
        updateToken();
        return envelopesApi.getEnvelope(accountId, envelopeId);
    }

    @Override
    public byte[] getDocumentPdf(String envelopeId, String documentId) throws ApiException {
        updateToken();
        return envelopesApi.getDocument(accountId, envelopeId, documentId);
    }

    @Override
    public void resendEnvelope(String envelopeId) throws ApiException {
        updateToken();

        // pega todos os destinatários
        Recipients recipients = envelopesApi.listRecipients(accountId, envelopeId);

        // filtra apenas os signatários pendentes
        List<Signer> pendingSigners = recipients.getSigners().stream()
                .filter(s -> !"completed".equalsIgnoreCase(s.getStatus()))
                .toList();

        if (pendingSigners.isEmpty()) {
            throw new ApiException("Nenhum destinatário pendente para reenviar.");
        }

        Recipients resendRecipients = new Recipients();
        resendRecipients.setSigners(pendingSigners);

        // reenviar notificações
        envelopesApi.updateRecipients(accountId, envelopeId, resendRecipients);
    }

    @Override
    public void voidEnvelope(String envelopeId, String motivo) throws ApiException {
        updateToken();
        Envelope envelope = new Envelope();
        envelope.setStatus("voided");
        envelope.setVoidedReason(motivo);
        envelopesApi.update(accountId, envelopeId, envelope);
    }
}
