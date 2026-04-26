package ru.uust.iimrt.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
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

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<Void> handleUnauthorized() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Map<String, String>> handleMethodNotAllowed() {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(Map.of("detail", "Method Not Allowed"));
    }

    @ExceptionHandler(UnknownDrinkException.class)
    public ResponseEntity<ErrorResponse> handleUnknownDrink(UnknownDrinkException e) {
        ErrorResponse response = new ErrorResponse("unknown_drink");
        response.setBalance(e.getBalance());
        response.setMood_level(e.getMood().toString());
        return ResponseEntity.ok(response);
    }

    @ExceptionHandler(UnknownRecipeException.class)
    public ResponseEntity<ErrorResponse> handleUnknownRecipe(UnknownRecipeException e) {
        ErrorResponse response = new ErrorResponse("unknown_recipe");
        response.setBalance(e.getBalance());
        response.setMood_level(e.getMood().toString());
        return ResponseEntity.ok(response);
    }

    @ExceptionHandler(InsufficientFundsException.class)
    public ResponseEntity<ErrorResponse> handleInsufficientFunds(InsufficientFundsException e) {
        ErrorResponse response = new ErrorResponse("insufficient_funds");
        response.setPrice(e.getPrice());
        response.setBalance(e.getBalance());
        response.setMood_level(e.getMood().toString());
        return ResponseEntity.ok(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneral() {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("internal_error"));
    }
}

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"status", "error", "price", "balance", "mood_level"})
class ErrorResponse {
    private String status = "error";
    private String error;
    private Integer price;
    private Integer balance;
    private String mood_level;

    public ErrorResponse(String error) {
        this.error = error;
    }
}