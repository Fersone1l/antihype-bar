package ru.uust.iimrt.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class User {
    private String token;
    private String id;
    private Rank rank;
    private int balance;
    private boolean isBarClosed;
    private BarmenMoods barmenMood;
    private boolean secretUnlocked;  // ← новое поле

    public User(String token, String id, Rank rank, int balance, boolean isBarClosed, BarmenMoods barmenMood) {
        this.token = token;
        this.id = id;
        this.rank = rank;
        this.balance = balance;
        this.isBarClosed = isBarClosed;
        this.barmenMood = barmenMood;
        this.secretUnlocked = false;
    }
}