package ru.uust.iimrt.model;

import lombok.Getter;

@Getter
public enum Rank {
    BEGINNER("Новичок", 0),
    GUEST("Гость", 3),
    REGULAR("Постоянный", 5),
    CONNOISSEUR("Знаток", 8),
    MASTER("Мастер", 12);

    private final String displayName;
    private final int requiredUniqueDrinks;

    Rank(String displayName, int requiredUniqueDrinks) {
        this.displayName = displayName;
        this.requiredUniqueDrinks = requiredUniqueDrinks;
    }

    /**
     * Получить ранг по количеству уникальных напитков
     */
    public static Rank fromUniqueDrinks(int uniqueCount) {
        Rank result = BEGINNER;
        for (Rank rank : values()) {
            if (uniqueCount >= rank.requiredUniqueDrinks) {
                result = rank;
            }
        }
        return result;
    }
}