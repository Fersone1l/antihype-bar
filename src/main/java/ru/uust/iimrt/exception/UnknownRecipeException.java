// UnknownRecipeException.java
package ru.uust.iimrt.exception;

public class UnknownRecipeException extends RuntimeException {
    public UnknownRecipeException(String message) {
        super(message);
    }
}