// UnauthorizedException.java
package ru.uust.iimrt.exception;

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String message) {
        super(message);
    }
}