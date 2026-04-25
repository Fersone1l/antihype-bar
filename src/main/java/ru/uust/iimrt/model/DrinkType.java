package ru.uust.iimrt.model;

import lombok.Getter;

@Getter
public enum DrinkType {
    CUBA_LIBRE("Куба Либре"),
    OTVERTKA("Отвёртка"),
    GIN_TONIC("Джин-тоник"),
    WHISKY_COLA("Виски-кола"),
    TEQUILA_SUNRISE("Текила-санрайз"),
    RUSSIAN("Русский"),
    WHITE_RUSSIAN("Белый русский"),
    LONG_ISLAND("Лонг-Айленд");

    private final String russianName;

    DrinkType(String russianName) {
        this.russianName = russianName;
    }

    public String getEnglishName() {
        return this.name().toLowerCase().replace('_', ' ');
    }

    @Override
    public String toString() {
        return russianName;
    }
}