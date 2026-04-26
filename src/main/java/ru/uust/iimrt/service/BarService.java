package ru.uust.iimrt.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.uust.iimrt.dto.response.*;
import ru.uust.iimrt.exception.InsufficientFundsException;
import ru.uust.iimrt.exception.UnauthorizedException;
import ru.uust.iimrt.exception.UnknownDrinkException;
import ru.uust.iimrt.exception.UnknownRecipeException;
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

    // Счётчики для отслеживания "каждый 4-й русский"
    private final Map<String, Integer> russianCounters = new HashMap<>();

    /**
     * Получить меню с ценами в зависимости от настроения и времени
     */
    public MenuResponse getMenu(String token, String time) {
        User user = userStorage.getUserByToken(token);
        if (user == null) {
            throw new UnauthorizedException("User not found");
        }

        String validTime = TimeUtils.extractTime(time);
        BarmenMoods mood = calculateMood(user, validTime);
        boolean isNight = TimeUtils.isNightTime(validTime);
        DrinkType favorite = barStorage.getFavoriteDrink(token);

        LinkedHashMap<DrinkType, Integer> prices = Menu.getMenu(mood, isNight, favorite);

        List<IngredientsMenuResponse> drinks = new ArrayList<>();
        for (var entry : prices.entrySet()) {
            DrinkType drinkType = entry.getKey();

            if (mood == BarmenMoods.HOSTILE && isComplexDrink(drinkType)) {
                continue;
            }

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
            throw new UnauthorizedException("User not found");
        }

        String validTime = TimeUtils.extractTime(time);
        BarmenMoods mood = calculateMood(user, validTime);
        boolean isNight = TimeUtils.isNightTime(validTime);
        DrinkType favorite = barStorage.getFavoriteDrink(token);

        // HOSTILE отказывается
        if (mood == BarmenMoods.HOSTILE && isComplexDrink(drink)) {
            throw new UnknownDrinkException("Bartender refuses to make this drink");
        }

        // Проверка времени
        if (isNight && !drink.isNightOnly()) {
            throw new UnknownDrinkException("Not available at night");
        }
        if (!isNight && drink.isNightOnly()) {
            throw new UnknownDrinkException("Only available at night");
        }

        boolean isFavorite = (drink == favorite);
        int price = drink.getPrice(mood, isFavorite);

        // Недостаточно средств
        if (user.getBalance() < price) {
            throw new InsufficientFundsException("Not enough money");
        }

        barStorage.makeOrder(token, drink, mood, "order", isNight, isFavorite);
        user.setBalance(user.getBalance() - price);

        if (drink == DrinkType.RUSSIAN) {
            int count = russianCounters.getOrDefault(token, 0) + 1;
            russianCounters.put(token, count);
            if (count % 4 == 0) {
                user.setBarmenMood(user.getBarmenMood().shift(1));
                russianCounters.put(token, 0);
            }
        }

        updateUserStats(user, validTime);
        checkMoodChange(user, validTime);

        return new OrderResponse(drink, price, user.getBalance(), user.getBarmenMood());
    }

    /**
     * Смешать коктейль из ингредиентов
     */
    public MixResponse mixDrinks(String token, String time, List<String> ingredientNames) {
        User user = userStorage.getUserByToken(token);
        if (user == null) {
            throw new UnauthorizedException("User not found");
        }

        String validTime = TimeUtils.extractTime(time);
        BarmenMoods mood = calculateMood(user, validTime);
        boolean isNight = TimeUtils.isNightTime(validTime);

        List<Ingredient> ingredients;
        try {
            ingredients = ingredientNames.stream()
                    .map(Ingredient::fromRussianName)
                    .toList();
        } catch (IllegalArgumentException e) {
            throw new UnknownRecipeException("Unknown ingredient");
        }

        // Секретные миксы
        BarmenMoods newMood = checkSecretMix(ingredients, mood);
        if (newMood != null) {
            user.setBarmenMood(newMood);
            return new MixResponse(
                    findDrinkByIngredients(ingredients),
                    0,
                    user.getBalance(),
                    user.getBarmenMood()
            );
        }

        var foundDrink = RecipeMenu.findByIngredients(ingredients);
        if (foundDrink.isEmpty()) {
            throw new UnknownRecipeException("Unknown combination");
        }

        DrinkType drink = foundDrink.get();

        if (mood == BarmenMoods.HOSTILE && isComplexDrink(drink)) {
            throw new UnknownRecipeException("Bartender refuses");
        }

        if (isNight && !drink.isNightOnly()) {
            throw new UnknownRecipeException("Not available at night");
        }
        if (!isNight && drink.isNightOnly()) {
            throw new UnknownRecipeException("Only available at night");
        }

        DrinkType favorite = barStorage.getFavoriteDrink(token);
        boolean isFavorite = (drink == favorite);
        int price = drink.getPrice(mood, isFavorite);

        if (user.getBalance() < price) {
            throw new InsufficientFundsException("Not enough money");
        }

        barStorage.makeOrder(token, drink, mood, "mix", isNight, isFavorite);
        user.setBalance(user.getBalance() - price);
        updateUserStats(user, validTime);
        checkMoodChange(user, validTime);

        return new MixResponse(drink, price, user.getBalance(), user.getBarmenMood());
    }

    /**
     * Проверка секретных комбинаций для /mix
     */
    private BarmenMoods checkSecretMix(List<Ingredient> ingredients, BarmenMoods currentMood) {
        Set<Ingredient> set = new HashSet<>(ingredients);

        // ["текила", "лёд", "молоко"] → GENEROUS
        if (set.equals(Set.of(Ingredient.TEQUILA, Ingredient.ICE, Ingredient.MILK))) {
            return BarmenMoods.GENEROUS;
        }

        // ["водка", "ром", "текила", "виски", "джин"] → HOSTILE
        if (set.equals(Set.of(Ingredient.VODKA, Ingredient.RUM, Ingredient.TEQUILA,
                Ingredient.WHISKY, Ingredient.GIN))) {
            return BarmenMoods.HOSTILE;
        }

        // ["водка", "ром", "молоко"] → зависит от текущего настроения
        if (set.equals(Set.of(Ingredient.VODKA, Ingredient.RUM, Ingredient.MILK))) {
            return switch (currentMood) {
                case GENEROUS -> BarmenMoods.FRIENDLY;
                case FRIENDLY -> BarmenMoods.GRUMPY;
                case NORMAL -> BarmenMoods.HOSTILE;
                case GRUMPY -> BarmenMoods.HOSTILE;
                default -> currentMood;
            };
        }

        return null;
    }

    /**
     * Найти напиток по ингредиентам (для секретных миксов)
     */
    private DrinkType findDrinkByIngredients(List<Ingredient> ingredients) {
        var found = RecipeMenu.findByIngredients(ingredients);
        return found.orElse(DrinkType.RUSSIAN); // fallback
    }

    /**
     * Сложные коктейли, от которых HOSTILE отказывается
     */
    private boolean isComplexDrink(DrinkType drink) {
        return drink == DrinkType.LONG_ISLAND || drink == DrinkType.WHITE_RUSSIAN;
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

        return baseMood;
    }

    /**
     * Обновление статистики пользователя
     */
    private void updateUserStats(User user, String time) {
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
    }
}