package ru.uust.iimrt.exception;

import lombok.Getter;
import ru.uust.iimrt.model.BarmenMoods;

@Getter
public class InsufficientFundsException extends RuntimeException {
    private final int price;
    private final int balance;
    private final BarmenMoods mood;

    public InsufficientFundsException(int price, int balance, BarmenMoods mood) {
        super("Not enough money");
        this.price = price;
        this.balance = balance;
        this.mood = mood;
    }
}