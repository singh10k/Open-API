package com.som.in.exceptions;

public class CustomEntryException extends RuntimeException{

    private static final long serialVersionUID = 1L;
    public CustomEntryException() {
        super();
    }
    public CustomEntryException(String message, Throwable cause, boolean enableSuppression,
                                boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
    public CustomEntryException(String message, Throwable cause) {
        super(message, cause);
    }
    public CustomEntryException(String message) {
        super(message);
    }
    public CustomEntryException(Throwable cause) {
        super(cause);
    }
}
