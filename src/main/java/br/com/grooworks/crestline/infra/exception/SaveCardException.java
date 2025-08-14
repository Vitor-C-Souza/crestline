package br.com.grooworks.crestline.infra.exception;

public class SaveCardException extends RuntimeException {
    private final String message;

    public SaveCardException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
