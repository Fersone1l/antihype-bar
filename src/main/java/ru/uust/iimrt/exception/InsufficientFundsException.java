// InsufficientFundsException.java
package ru.uust.iimrt.exception;

public class InsufficientFundsException extends RuntimeException {
    public InsufficientFundsException(String message) {
        super(message);
    }
}