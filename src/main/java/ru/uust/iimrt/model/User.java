package ru.uust.iimrt.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class User {
    private String token;
    private String id;
    private Rank rank;
    private int balance;
    private boolean isBarClosed;
    private BarmenMoods barmenMood;
    private boolean secretUnlocked;
    private LocalDateTime barClosedUntil;  // ← время до которого бар закрыт

    public User(String token, String id, Rank rank, int balance, boolean isBarClosed, BarmenMoods barmenMood) {
        this.token = token;
        this.id = id;
        this.rank = rank;
        this.balance = balance;
        this.isBarClosed = isBarClosed;
        this.barmenMood = barmenMood;
        this.secretUnlocked = false;
        this.barClosedUntil = null;
    }

    /**
     * Проверяет, закрыт ли бар прямо сейчас
     */
    public boolean isBarClosed() {
        if (barClosedUntil == null) return false;
        if (LocalDateTime.now().isAfter(barClosedUntil)) {
            barClosedUntil = null;
            return false;
        }
        return true;
    }
}