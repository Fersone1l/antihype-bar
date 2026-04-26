package ru.uust.iimrt.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.uust.iimrt.exception.InsufficientFundsException;
import ru.uust.iimrt.exception.UnauthorizedException;
import ru.uust.iimrt.exception.UnknownDrinkException;
import ru.uust.iimrt.exception.UnknownRecipeException;

import java.util.Map;


@ControllerAdvice
public class GlobalExceptionHandler {

    // 401 — неавторизован (пустое тело)
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<Void> handleUnauthorized() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    // 405 — Method Not Allowed
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Map<String, String>> handleMethodNotAllowed() {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(Map.of("detail", "Method Not Allowed"));
    }

    // unknown_drink
    @ExceptionHandler(UnknownDrinkException.class)
    public ResponseEntity<ErrorResponse> handleUnknownDrink() {
        return ResponseEntity.ok(new ErrorResponse("unknown_drink"));
    }

    // unknown_recipe
    @ExceptionHandler(UnknownRecipeException.class)
    public ResponseEntity<ErrorResponse> handleUnknownRecipe() {
        return ResponseEntity.ok(new ErrorResponse("unknown_recipe"));
    }

    // insufficient_funds
    @ExceptionHandler(InsufficientFundsException.class)
    public ResponseEntity<ErrorResponse> handleInsufficientFunds() {
        return ResponseEntity.ok(new ErrorResponse("insufficient_funds"));
    }

    // Все остальные ошибки — 500
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneral() {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("internal_error"));
    }
}

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
class ErrorResponse {
    private String status = "error";
    private String error;

    public ErrorResponse(String error) {
        this.error = error;
    }
}