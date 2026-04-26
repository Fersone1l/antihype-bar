package ru.uust.iimrt.model;

import lombok.Getter;
import java.util.*;

@Getter
public enum DrinkType {
    // ========== ОБЫЧНЫЕ НАПИТКИ ==========
    CUBA_LIBRE("Куба Либре", false, false,
            Map.of(
                    BarmenMoods.HOSTILE, 23,
                    BarmenMoods.GRUMPY, 18,
                    BarmenMoods.NORMAL, 15,
                    BarmenMoods.FRIENDLY, 13,
                    BarmenMoods.GENEROUS, 11
            ),
            Map.of(
                    BarmenMoods.HOSTILE, 18,
                    BarmenMoods.GRUMPY, 15,
                    BarmenMoods.NORMAL, 12,
                    BarmenMoods.FRIENDLY, 10,
                    BarmenMoods.GENEROUS, 9
            ),
            List.of(Ingredient.COLA, Ingredient.ICE, Ingredient.RUM)
    ),

    OTVERTKA("Отвёртка", false, false,
            Map.of(
                    BarmenMoods.HOSTILE, 18,
                    BarmenMoods.GRUMPY, 15,
                    BarmenMoods.NORMAL, 12,
                    BarmenMoods.FRIENDLY, 10,
                    BarmenMoods.GENEROUS, 9
            ),
            Map.of(
                    BarmenMoods.HOSTILE, 14,
                    BarmenMoods.GRUMPY, 11,
                    BarmenMoods.NORMAL, 9,
                    BarmenMoods.FRIENDLY, 8,
                    BarmenMoods.GENEROUS, 6
            ),
            List.of(Ingredient.VODKA, Ingredient.JUICE)
    ),

    GIN_TONIC("Джин-тоник", false, false,
            Map.of(
                    BarmenMoods.HOSTILE, 21,
                    BarmenMoods.GRUMPY, 17,
                    BarmenMoods.NORMAL, 14,
                    BarmenMoods.FRIENDLY, 12,
                    BarmenMoods.GENEROUS, 10
            ),
            Map.of(
                    BarmenMoods.HOSTILE, 17,
                    BarmenMoods.GRUMPY, 14,
                    BarmenMoods.NORMAL, 11,
                    BarmenMoods.FRIENDLY, 9,
                    BarmenMoods.GENEROUS, 8
            ),
            List.of(Ingredient.GIN, Ingredient.ICE, Ingredient.TONIC)
    ),

    WHISKY_COLA("Виски-кола", false, false,
            Map.of(
                    BarmenMoods.HOSTILE, 20,
                    BarmenMoods.GRUMPY, 16,
                    BarmenMoods.NORMAL, 13,
                    BarmenMoods.FRIENDLY, 11,
                    BarmenMoods.GENEROUS, 9
            ),
            Map.of(
                    BarmenMoods.HOSTILE, 15,
                    BarmenMoods.GRUMPY, 12,
                    BarmenMoods.NORMAL, 10,
                    BarmenMoods.FRIENDLY, 9,
                    BarmenMoods.GENEROUS, 7
            ),
            List.of(Ingredient.WHISKY, Ingredient.COLA)
    ),

    TEQUILA_SUNRISE("Текила-санрайз", false, false,
            Map.of(
                    BarmenMoods.HOSTILE, 21,
                    BarmenMoods.GRUMPY, 17,
                    BarmenMoods.NORMAL, 14,
                    BarmenMoods.FRIENDLY, 12,
                    BarmenMoods.GENEROUS, 10
            ),
            Map.of(
                    BarmenMoods.HOSTILE, 14,
                    BarmenMoods.GRUMPY, 9,
                    BarmenMoods.NORMAL, 11,
                    BarmenMoods.FRIENDLY, 8,
                    BarmenMoods.GENEROUS, 8
            ),
            List.of(Ingredient.JUICE, Ingredient.TEQUILA)
    ),

    RUSSIAN("Русский", false, false,
            Map.of(
                    BarmenMoods.HOSTILE, 15,
                    BarmenMoods.GRUMPY, 12,
                    BarmenMoods.NORMAL, 10,
                    BarmenMoods.FRIENDLY, 9,
                    BarmenMoods.GENEROUS, 7
            ),
            Map.of(
                    BarmenMoods.HOSTILE, 11,
                    BarmenMoods.GRUMPY, 9,
                    BarmenMoods.NORMAL, 7,
                    BarmenMoods.FRIENDLY, 6,
                    BarmenMoods.GENEROUS, 5
            ),
            List.of(Ingredient.VODKA, Ingredient.ICE)
    ),

    WHITE_RUSSIAN("Белый русский", false, false,
            Map.of(
                    BarmenMoods.HOSTILE, 24,
                    BarmenMoods.GRUMPY, 20,
                    BarmenMoods.NORMAL, 16,
                    BarmenMoods.FRIENDLY, 14,
                    BarmenMoods.GENEROUS, 12
            ),
            Map.of(
                    BarmenMoods.HOSTILE, 16,
                    BarmenMoods.GRUMPY, 16,
                    BarmenMoods.NORMAL, 13,
                    BarmenMoods.FRIENDLY, 11,
                    BarmenMoods.GENEROUS, 9
            ),
            List.of(Ingredient.VODKA, Ingredient.ICE, Ingredient.MILK)
    ),

    LONG_ISLAND("Лонг-Айленд", false, false,
            Map.of(
                    BarmenMoods.HOSTILE, 38,
                    BarmenMoods.GRUMPY, 30,
                    BarmenMoods.NORMAL, 25,
                    BarmenMoods.FRIENDLY, 22,
                    BarmenMoods.GENEROUS, 18
            ),
            Map.of(
                    BarmenMoods.HOSTILE, 30,
                    BarmenMoods.GRUMPY, 25,
                    BarmenMoods.NORMAL, 22,
                    BarmenMoods.FRIENDLY, 20,
                    BarmenMoods.GENEROUS, 15
            ),
            List.of(Ingredient.VODKA, Ingredient.GIN, Ingredient.COLA,
                    Ingredient.RUM, Ingredient.TEQUILA)
    ),

    // ========== НОЧНЫЕ НАПИТКИ (только 00:00-06:00) ==========
    NIGHT_RUSSIAN("Ночной русский", true, false,
            Map.of(
                    BarmenMoods.HOSTILE, 9,
                    BarmenMoods.GRUMPY, 8,
                    BarmenMoods.NORMAL, 6,
                    BarmenMoods.FRIENDLY, 5,
                    BarmenMoods.GENEROUS, 4
            ),
            Map.of(
                    BarmenMoods.HOSTILE, 5,
                    BarmenMoods.GRUMPY, 4,
                    BarmenMoods.NORMAL, 3,
                    BarmenMoods.FRIENDLY, 2,
                    BarmenMoods.GENEROUS, 2
            ),
            List.of(Ingredient.VODKA, Ingredient.ICE, Ingredient.MILK)
    ),

    INSOMNIA("Бессонница", true, false,
            Map.of(
                    BarmenMoods.HOSTILE, 12,
                    BarmenMoods.GRUMPY, 10,
                    BarmenMoods.NORMAL, 8,
                    BarmenMoods.FRIENDLY, 7,
                    BarmenMoods.GENEROUS, 6
            ),
            Map.of(
                    BarmenMoods.HOSTILE, 8,
                    BarmenMoods.GRUMPY, 6,
                    BarmenMoods.NORMAL, 5,
                    BarmenMoods.FRIENDLY, 4,
                    BarmenMoods.GENEROUS, 3
            ),
            List.of(Ingredient.COLA, Ingredient.RUM, Ingredient.TONIC)
    ),

    MOONLIGHT("Лунный свет", true, false,
            Map.of(
                    BarmenMoods.HOSTILE, 15,
                    BarmenMoods.GRUMPY, 12,
                    BarmenMoods.NORMAL, 10,
                    BarmenMoods.FRIENDLY, 9,
                    BarmenMoods.GENEROUS, 7
            ),
            Map.of(
                    BarmenMoods.HOSTILE, 11,
                    BarmenMoods.GRUMPY, 9,
                    BarmenMoods.NORMAL, 7,
                    BarmenMoods.FRIENDLY, 6,
                    BarmenMoods.GENEROUS, 5
            ),
            List.of(Ingredient.GIN, Ingredient.JUICE, Ingredient.TONIC)
    );

    private final String russianName;
    private final boolean nightOnly;      // Только ночью?
    private final boolean secret;         // Секретный?
    private final Map<BarmenMoods, Integer> prices;          // Обычные цены
    private final Map<BarmenMoods, Integer> favoritePrices;  // Цены для любимого
    private final List<Ingredient> ingredients;

    DrinkType(String russianName,
              boolean nightOnly,
              boolean secret,
              Map<BarmenMoods, Integer> prices,
              Map<BarmenMoods, Integer> favoritePrices,
              List<Ingredient> ingredients) {
        this.russianName = russianName;
        this.nightOnly = nightOnly;
        this.secret = secret;
        this.prices = prices;
        this.favoritePrices = favoritePrices;
        this.ingredients = ingredients;
    }

    /**
     * Получить цену напитка
     * @param mood настроение бармена
     * @param isFavorite это любимый напиток?
     */
    public int getPrice(BarmenMoods mood, boolean isFavorite) {
        if (secret) return 0;
        return isFavorite
                ? favoritePrices.getOrDefault(mood, 0)
                : prices.getOrDefault(mood, 0);
    }

    public static DrinkType fromRussianName(String russianName) {
        for (DrinkType type : values()) {
            if (type.russianName.equalsIgnoreCase(russianName)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown drink: " + russianName);
    }

    @Override
    public String toString() {
        return russianName;
    }
}