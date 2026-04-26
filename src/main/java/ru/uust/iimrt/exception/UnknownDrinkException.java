package ru.uust.iimrt.exception;

import lombok.Getter;
import ru.uust.iimrt.model.BarmenMoods;

@Getter
public class UnknownDrinkException extends RuntimeException {
    private final int balance;
    private final BarmenMoods mood;

    public UnknownDrinkException(int balance, BarmenMoods mood) {
        super("Unknown drink");
        this.balance = balance;
        this.mood = mood;
    }
}