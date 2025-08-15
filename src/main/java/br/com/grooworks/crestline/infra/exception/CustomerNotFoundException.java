package br.com.grooworks.crestline.infra.exception;

public class CustomerNotFoundException extends RuntimeException {
    private final String message;

    public CustomerNotFoundException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
