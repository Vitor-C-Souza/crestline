package br.com.grooworks.crestline.infra.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class Exceptions {

    @ExceptionHandler(SaveCardException.class)
    public ResponseEntity<ResponseError> handleSaveCardException(SaveCardException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UpdateCardException.class)
    public ResponseEntity<ResponseError> handleUpdateCardException(UpdateCardException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(deleteCreditCardException.class)
    public ResponseEntity<ResponseError> handleDeleteCardException(deleteCreditCardException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PaymentException.class)
    public ResponseEntity<ResponseError> handlePaymentException(PaymentException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.PAYMENT_REQUIRED);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ResponseError> handleRuntimeException(RuntimeException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    
    private ResponseEntity<ResponseError> buildResponse(String message, HttpStatus status) {
        return ResponseEntity.status(status)
                .body(new ResponseError(message, status, LocalDateTime.now()));
    }
}
