package br.com.grooworks.crestline.infra.exception;

public class deleteCreditCardException extends RuntimeException {
    private final String message;

    public deleteCreditCardException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
