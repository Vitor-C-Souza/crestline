package br.com.grooworks.crestline.domain.service.impl;

import br.com.grooworks.crestline.domain.dto.SendContractDto;
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

    public DocuSignServiceImpl(
            @Value("${docusign.account-id}") String accountId,
            @Value("${docusign.access-token}") String accessToken
    ) {
        this.accountId = accountId;
        this.apiClient = new ApiClient("https://demo.docusign.net/restapi");
        this.apiClient.addDefaultHeader("Authorization", "Bearer " + accessToken);
        this.envelopesApi = new EnvelopesApi(apiClient);
    }

    @Override
    public String sendEnvelope(SendContractDto dto, byte[] file, String fileName, String fileExt) throws ApiException {
        EnvelopeDefinition envelope = new EnvelopeDefinition();
        envelope.setEmailSubject("Assinatura: " + dto.title());

        // Documento
        Document doc = new Document();
        doc.setDocumentBase64(Base64.getEncoder().encodeToString(file));
        doc.setName(fileName);
        doc.setFileExtension(fileExt);
        doc.setDocumentId("1");

        // Signatário
        Signer signer = new Signer();
        signer.setEmail(dto.signerEmail());
        signer.setName(dto.signerName());
        signer.setRecipientId("1");

        SignHere signHere = new SignHere();
        signHere.setAnchorString("/sig/"); // marcador no PDF
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
        envelope.setStatus("sent"); // já envia

        EnvelopeSummary result = envelopesApi.createEnvelope(this.accountId, envelope);
        return result.getEnvelopeId();
    }

    @Override
    public Envelope getEnvelope(String envelopeId) throws ApiException {
        return envelopesApi.getEnvelope(this.accountId, envelopeId);
    }

    @Override
    public byte[] getDocumentPdf(String envelopeId, String documentId) throws ApiException {
        byte[] docBytes = envelopesApi.getDocument(this.accountId, envelopeId, documentId);
        return docBytes;
    }

    @Override
    public void resendEnvelope(String envelopeId) throws ApiException {
        envelopesApi.updateRecipients(this.accountId, envelopeId, new Recipients());
    }

    @Override
    public void voidEnvelope(String envelopeId, String motivo) throws ApiException {
        Envelope envelope = new Envelope();
        envelope.setStatus("voided");
        envelope.setVoidedReason(motivo);
        envelopesApi.update(this.accountId, envelopeId, envelope);
    }
}
