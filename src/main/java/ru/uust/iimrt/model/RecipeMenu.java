package ru.uust.iimrt.model;

import java.util.*;

public class RecipeMenu {
    public static final Map<DrinkType, List<Ingredient>> menu;

    static {
        menu = new HashMap<>();

        // Куба Либре
        menu.put(DrinkType.CUBA_LIBRE, List.of(
                Ingredient.COLA, Ingredient.ICE, Ingredient.RUM
        ));

        // Отвёртка
        menu.put(DrinkType.OTVERTKA, List.of(
                Ingredient.VODKA, Ingredient.JUICE
        ));

        // Джин-тоник
        menu.put(DrinkType.GIN_TONIC, List.of(
                Ingredient.GIN, Ingredient.ICE, Ingredient.TONIC
        ));

        // Виски-кола
        menu.put(DrinkType.WHISKY_COLA, List.of(
                Ingredient.WHISKY, Ingredient.COLA
        ));

        // Текила-санрайз
        menu.put(DrinkType.TEQUILA_SUNRISE, List.of(
                Ingredient.JUICE, Ingredient.TEQUILA
        ));

        // Русский
        menu.put(DrinkType.RUSSIAN, List.of(
                Ingredient.VODKA, Ingredient.ICE
        ));

        // Белый русский
        menu.put(DrinkType.WHITE_RUSSIAN, List.of(
                Ingredient.VODKA, Ingredient.ICE, Ingredient.MILK
        ));

        // Лонг-Айленд
        menu.put(DrinkType.LONG_ISLAND, List.of(
                Ingredient.VODKA, Ingredient.GIN, Ingredient.COLA,
                Ingredient.RUM, Ingredient.TEQUILA
        ));
        // В static блок добавить:

// Воздух
        menu.put(DrinkType.VOZDOKH, List.of());

// Мертвец
        menu.put(DrinkType.MERTVEC, List.of(
                Ingredient.VODKA, Ingredient.RUM, Ingredient.MILK
        ));

// Ошибка бармена
        menu.put(DrinkType.OSHIBA_BARMENA, List.of(
                Ingredient.TEQUILA, Ingredient.ICE, Ingredient.MILK
        ));

// Зелье бармена
        menu.put(DrinkType.ZELYE_BARMENA, List.of(
                Ingredient.GIN, Ingredient.JUICE, Ingredient.TONIC, Ingredient.ICE
        ));

// Армагеддон
        menu.put(DrinkType.ARMAGEDDON, List.of(
                Ingredient.VODKA, Ingredient.RUM, Ingredient.TEQUILA,
                Ingredient.WHISKY, Ingredient.GIN
        ));
    }

    /**
     * Поиск напитка по ингредиентам (для /mix)
     */
    public static Optional<DrinkType> findByIngredients(List<Ingredient> ingredients) {
        // Сортируем для сравнения без учета порядка
        Set<Ingredient> searchSet = new HashSet<>(ingredients);

        for (Map.Entry<DrinkType, List<Ingredient>> entry : menu.entrySet()) {
            Set<Ingredient> recipeSet = new HashSet<>(entry.getValue());
            if (searchSet.equals(recipeSet)) {
                return Optional.of(entry.getKey());
            }
        }
        return Optional.empty();
    }
}