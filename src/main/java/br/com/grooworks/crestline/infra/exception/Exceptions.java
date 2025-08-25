package br.com.grooworks.crestline.infra.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class Exceptions {

    // 404 - quando não encontra algo
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ResponseError> handleNoSuchElement(NoSuchElementException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    // 400 - erro ao salvar cartão
    @ExceptionHandler(SaveCardException.class)
    public ResponseEntity<ResponseError> handleSaveCard(SaveCardException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    // 400 - erro ao atualizar cartão
    @ExceptionHandler(UpdateCardException.class)
    public ResponseEntity<ResponseError> handleUpdateCard(UpdateCardException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    // 400 - erro ao deletar cartão
    @ExceptionHandler(deleteCreditCardException.class)
    public ResponseEntity<ResponseError> handleDeleteCard(deleteCreditCardException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    // 400 - erro de pagamento
    @ExceptionHandler(PaymentException.class)
    public ResponseEntity<ResponseError> handlePayment(PaymentException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    // 403 - acesso negado
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ResponseError> handleAccessDenied(AccessDeniedException ex) {
        return buildResponse("Acesso negado: você não tem permissão para executar essa ação", HttpStatus.FORBIDDEN);
    }

    // 401 - não autenticado
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ResponseError> handleAuthenticationException(AuthenticationException ex) {
        return buildResponse("Não autorizado: você precisa se autenticar para acessar este recurso", HttpStatus.UNAUTHORIZED);
    }

    // 500 - qualquer outro runtime
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ResponseError> handleRuntime(RuntimeException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ResponseError> buildResponse(String message, HttpStatus status) {
        return ResponseEntity.status(status)
                .body(new ResponseError(message, status, LocalDateTime.now()));
    }
}
