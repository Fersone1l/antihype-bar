package ru.uust.iimrt.exception;

import lombok.Getter;
import ru.uust.iimrt.model.BarmenMoods;

@Getter
public class UnknownRecipeException extends RuntimeException {
    private final int balance;
    private final BarmenMoods mood;

    public UnknownRecipeException(int balance, BarmenMoods mood) {
        super("Unknown recipe");
        this.balance = balance;
        this.mood = mood;
    }
}