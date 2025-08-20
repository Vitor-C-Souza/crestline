package br.com.grooworks.crestline.infra.exception;

public class DocuSignSendException extends  RuntimeException {
    public DocuSignSendException(String message, Throwable cause) {
        super(message, cause);
    }
}
