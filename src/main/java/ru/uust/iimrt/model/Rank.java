package ru.uust.iimrt.model;

import lombok.Getter;

@Getter
public enum Rank {
    BEGINNER("Новичок"),
    AMATEUR("Любитель"),
    CONNOISSEUR("Знаток"),
    EXPERT("Эксперт"),
    MASTER("Мастер");

    private final String displayName;

    Rank(String displayName) {
        this.displayName = displayName;
    }
}