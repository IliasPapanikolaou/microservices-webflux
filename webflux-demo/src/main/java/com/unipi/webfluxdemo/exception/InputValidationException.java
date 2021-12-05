package com.unipi.webfluxdemo.exception;


public class InputValidationException extends RuntimeException {

    public static final String MSG = "Allowed range is 10 - 20";
    public static final int errorCode = 100;
    private final int input;

    public InputValidationException(int input) {
        super(MSG);
        this.input = input;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public int getInput() {
        return input;
    }
}
