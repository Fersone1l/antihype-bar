package ru.uust.iimrt.model;

import lombok.Getter;

@Getter
public enum Ingredient {
    COLA("кола"),
    ICE("лёд"),
    RUM("ром"),
    VODKA("водка"),
    JUICE("сок"),
    GIN("джин"),
    TONIC("тоник"),
    WHISKY("виски"),
    TEQUILA("текила"),
    MILK("молоко");

    private final String russianName;

    Ingredient(String russianName) {
        this.russianName = russianName;
    }

    public String getEnglishName() {
        return this.name().toLowerCase();
    }

    public static Ingredient fromRussianName(String russian) {
        for (Ingredient ingredient : values()) {
            if (ingredient.russianName.equals(russian)) {
                return ingredient;
            }
        }
        throw new IllegalArgumentException("Unknown ingredient: " + russian);
    }

    @Override
    public String toString() {
        return russianName;
    }
}