package ru.uust.iimrt.model;

import java.util.LinkedHashMap;

public class Menu {

    /**
     * Получить меню с учетом времени суток и любимого напитка
     */
    public static LinkedHashMap<DrinkType, Integer> getMenu(
            BarmenMoods mood, boolean isNight, DrinkType favorite) {

        LinkedHashMap<DrinkType, Integer> menu = new LinkedHashMap<>();

        for (DrinkType drink : DrinkType.values()) {
            // Секретные не показываем
            if (drink.isSecret()) continue;

            if (isNight) {
                // Ночью показываем ТОЛЬКО ночные напитки
                if (drink.isNightOnly()) {
                    boolean isFavorite = (drink == favorite);
                    menu.put(drink, drink.getPrice(mood, isFavorite));
                }
            } else {
                // Днём показываем ТОЛЬКО обычные напитки
                if (!drink.isNightOnly()) {
                    boolean isFavorite = (drink == favorite);
                    menu.put(drink, drink.getPrice(mood, isFavorite));
                }
            }
        }

        return menu;
    }

    public static int getPrice(BarmenMoods mood, DrinkType drink, boolean isFavorite) {
        return drink.getPrice(mood, isFavorite);
    }

    public static String getNote(BarmenMoods mood) {
        return switch (mood) {
            case GENEROUS -> "Я сегодня добрый. Каждый третий — за счёт заведения.";
            case FRIENDLY -> "Попробуй смешать что-нибудь сам — будет дешевле.";
            case HOSTILE -> "Цены сегодня кусаются. Но ты всегда можешь уйти.";
            case GRUMPY -> "Не нравится — не заказывай.";
            default -> null;
        };
    }

    public static boolean isNightTime(String time) {
        int hour = Integer.parseInt(time.split(":")[0]);
        return hour >= 0 && hour < 6;
    }
}