package com.AustinPilz.FridayThe13th.Exceptions;

public class EscapePointSessionAlreadyInProgressException extends Exception {
    public EscapePointSessionAlreadyInProgressException() {
    }

    // Constructor that accepts a message
    public EscapePointSessionAlreadyInProgressException(String message) {
        super(message);
    }
}
