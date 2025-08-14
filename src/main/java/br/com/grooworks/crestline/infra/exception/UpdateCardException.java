package br.com.grooworks.crestline.infra.exception;

public class UpdateCardException extends RuntimeException {
    private final String message;

    public UpdateCardException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
