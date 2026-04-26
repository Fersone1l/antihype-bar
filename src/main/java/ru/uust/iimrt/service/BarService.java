package ru.uust.iimrt.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.uust.iimrt.dto.response.*;
import ru.uust.iimrt.model.*;
import ru.uust.iimrt.storage.BarStorage;
import ru.uust.iimrt.storage.UserStorage;
import ru.uust.iimrt.util.TimeUtils;

import java.util.*;

@Service
@RequiredArgsConstructor
public class BarService {
    private final BarStorage barStorage;
    private final UserStorage userStorage;

    /**
     * Получить меню с ценами в зависимости от настроения и времени
     */
    public MenuResponse getMenu(String token, String time) {
        User user = userStorage.getUserByToken(token);
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        String validTime = TimeUtils.extractTime(time);
        BarmenMoods mood = calculateMood(user, validTime);
        boolean isNight = TimeUtils.isNightTime(validTime);
        DrinkType favorite = barStorage.getFavoriteDrink(token);

        LinkedHashMap<DrinkType, Integer> prices = Menu.getMenu(mood, isNight, favorite);

        List<IngredientsMenuResponse> drinks = new ArrayList<>();
        for (var entry : prices.entrySet()) {
            DrinkType drinkType = entry.getKey();

            IngredientsMenuResponse drink = new IngredientsMenuResponse();
            drink.setName(drinkType.getRussianName());
            drink.setPrice(entry.getValue());
            drink.setIngredients(
                    drinkType.getIngredients().stream()
                            .map(Ingredient::getRussianName)
                            .toList()
            );

            drinks.add(drink);
        }

        MenuResponse response = new MenuResponse();
        response.setDrinks(drinks);
        response.setBalance(user.getBalance());
        response.setMood_level(mood.toString());
        response.setNote(Menu.getNote(mood));

        return response;
    }

    /**
     * Сделать заказ напитка
     */
    public OrderResponse makeOrder(String token, String time, DrinkType drink) {
        User user = userStorage.getUserByToken(token);
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        String validTime = TimeUtils.extractTime(time);
        BarmenMoods mood = calculateMood(user, validTime);
        boolean isNight = TimeUtils.isNightTime(validTime);
        DrinkType favorite = barStorage.getFavoriteDrink(token);

        // Проверяем доступность напитка в зависимости от времени суток
        if (isNight && !drink.isNightOnly()) {
            return OrderResponse.error("unknown_drink", user.getBalance(), mood);
        }
        if (!isNight && drink.isNightOnly()) {
            return OrderResponse.error("unknown_drink", user.getBalance(), mood);
        }

        boolean isFavorite = (drink == favorite);
        int price = drink.getPrice(mood, isFavorite);

        // Проверяем баланс
        if (user.getBalance() < price) {
            return OrderResponse.error("insufficient_funds", price, user.getBalance(), mood);
        }

        // Выполняем заказ
        barStorage.makeOrder(token, drink, mood, "order", isNight, isFavorite);
        user.setBalance(user.getBalance() - price);

        // Обновляем статистику
        updateUserStats(user);

        // Проверяем изменение настроения
        checkMoodChange(user, validTime);

        return new OrderResponse(drink, price, user.getBalance(), user.getBarmenMood());
    }

    /**
     * Смешать коктейль из ингредиентов
     */
    public MixResponse mixDrinks(String token, String time, List<String> ingredientNames) {
        User user = userStorage.getUserByToken(token);
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        String validTime = TimeUtils.extractTime(time);
        BarmenMoods mood = calculateMood(user, validTime);
        boolean isNight = TimeUtils.isNightTime(validTime);

        // Конвертируем русские названия в enum
        List<Ingredient> ingredients;
        try {
            ingredients = ingredientNames.stream()
                    .map(Ingredient::fromRussianName)
                    .toList();
        } catch (IllegalArgumentException e) {
            return MixResponse.error("unknown_recipe", user.getBalance(), mood);
        }

        // Ищем напиток по ингредиентам
        var foundDrink = RecipeMenu.findByIngredients(ingredients);
        if (foundDrink.isEmpty()) {
            return MixResponse.error("unknown_recipe", user.getBalance(), mood);
        }

        DrinkType drink = foundDrink.get();

        // Проверяем доступность напитка
        if (isNight && !drink.isNightOnly()) {
            return MixResponse.error("unknown_recipe", user.getBalance(), mood);
        }
        if (!isNight && drink.isNightOnly()) {
            return MixResponse.error("unknown_recipe", user.getBalance(), mood);
        }

        DrinkType favorite = barStorage.getFavoriteDrink(token);
        boolean isFavorite = (drink == favorite);
        int price = drink.getPrice(mood, isFavorite);

        // Проверяем баланс
        if (user.getBalance() < price) {
            return MixResponse.error("insufficient_funds", price, user.getBalance(), mood);
        }

        // Выполняем заказ через mix
        barStorage.makeOrder(token, drink, mood, "mix", isNight, isFavorite);
        user.setBalance(user.getBalance() - price);
        updateUserStats(user);
        checkMoodChange(user, validTime);

        return new MixResponse(drink, price, user.getBalance(), user.getBarmenMood());
    }

    /**
     * Расчет настроения бармена с учетом времени суток
     */
    private BarmenMoods calculateMood(User user, String time) {
        if (user.isBarClosed()) {
            return BarmenMoods.HOSTILE;
        }

        BarmenMoods baseMood = user.getBarmenMood();
        int hour;

        try {
            hour = Integer.parseInt(time.split(":")[0]);
        } catch (Exception e) {
            return baseMood;
        }

        // Ночное время (00:00 - 06:00) — бармен добрее
        if (hour >= 0 && hour < 6) {
            return baseMood.shift(1);
        }

        // Утреннее время (06:00 - 10:00) — бармен ворчливый
        if (hour >= 6 && hour < 10) {
            return baseMood.shift(-1);
        }

        // Вечернее время (18:00 - 22:00) — бармен дружелюбный
        if (hour >= 18 && hour < 22) {
            return baseMood.shift(1);
        }

        // Поздний вечер (22:00 - 00:00) — бармен щедрый
        if (hour >= 22) {
            return baseMood.shift(2);
        }

        // Днём — обычное настроение
        return baseMood;
    }

    /**
     * Обновление статистики пользователя
     */
    private void updateUserStats(User user) {
        int totalOrders = barStorage.getTotalOrders(user.getToken());

        // Повышаем ранг каждые 5 заказов
        if (totalOrders > 0 && totalOrders % 5 == 0) {
            Rank[] ranks = Rank.values();
            int currentIndex = user.getRank().ordinal();

            if (currentIndex < ranks.length - 1) {
                user.setRank(ranks[currentIndex + 1]);
            }
        }
    }

    /**
     * Проверка изменения настроения после заказа
     */
    private void checkMoodChange(User user, String time) {
        int totalOrders = barStorage.getTotalOrders(user.getToken());

        // Каждый 10-й заказ улучшает настроение
        if (totalOrders > 0 && totalOrders % 10 == 0) {
            user.setBarmenMood(user.getBarmenMood().shift(1));
        }

        // Каждый 3-й заказ ночью делает щедрее
        if (totalOrders > 0 && totalOrders % 3 == 0 && TimeUtils.isNightTime(time)) {
            user.setBarmenMood(user.getBarmenMood().shift(1));
        }
    }
}