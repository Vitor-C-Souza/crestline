package br.com.grooworks.crestline.domain.service;

import br.com.grooworks.crestline.domain.dto.SendContractDto;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.Envelope;

public interface DocuSignService {
    String sendEnvelope(SendContractDto dto, byte[] file, String fileName, String fileExt) throws ApiException;

    Envelope getEnvelope(String envelopeId) throws ApiException;

    byte[] getDocumentPdf(String envelopeId, String documentId) throws ApiException;

    void resendEnvelope(String envelopeId) throws ApiException;

    void voidEnvelope(String envelopeId, String motivo) throws ApiException;
}
