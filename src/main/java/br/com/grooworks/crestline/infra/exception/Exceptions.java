package br.com.grooworks.crestline.infra.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class Exceptions {

    @ExceptionHandler(ContractNotFoundException.class)
    public ResponseEntity<ResponseError> handleContractNotFound(ContractNotFoundException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DocuSignSendException.class)
    public ResponseEntity<ResponseError> handleDocuSignSend(DocuSignSendException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DocuSignResendException.class)
    public ResponseEntity<ResponseError> handleDocuSignResend(DocuSignResendException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(DocuSignDeleteException.class)
    public ResponseEntity<ResponseError> handleDocuSignDelete(DocuSignDeleteException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ResponseError> handleRuntime(RuntimeException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ResponseError> buildResponse(String message, HttpStatus status) {
        return ResponseEntity.status(status)
                .body(new ResponseError(message, status, LocalDateTime.now()));
    }
}
