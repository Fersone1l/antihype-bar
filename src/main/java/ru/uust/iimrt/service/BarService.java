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

    private final Map<String, Integer> russianCounters = new HashMap<>();

    public MenuResponse getMenu(String token, String time) {
        User user = userStorage.getUserByToken(token);
        if (user == null) {
            throw new UnauthorizedException("User not found");
        }

        String validTime = TimeUtils.extractTime(time);
        boolean isNight = TimeUtils.isNightTime(validTime);
        BarmenMoods mood = user.getBarmenMood();
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

    public OrderResponse makeOrder(String token, String time, DrinkType drink) {
        User user = userStorage.getUserByToken(token);
        if (user == null) {
            throw new UnauthorizedException("User not found");
        }

        String validTime = TimeUtils.extractTime(time);
        boolean isNight = TimeUtils.isNightTime(validTime);
        BarmenMoods mood = user.getBarmenMood();
        DrinkType favorite = barStorage.getFavoriteDrink(token);

        if (mood == BarmenMoods.HOSTILE && isComplexDrink(drink)) {
            throw new UnknownDrinkException(user.getBalance(), mood);
        }

        if (isNight && !drink.isNightOnly()) {
            throw new UnknownDrinkException(user.getBalance(), mood);
        }
        if (!isNight && drink.isNightOnly()) {
            throw new UnknownDrinkException(user.getBalance(), mood);
        }

        boolean isFavorite = (drink == favorite);
        int price = drink.getPrice(mood, isFavorite);

        if (user.getBalance() < price) {
            throw new InsufficientFundsException(price, user.getBalance(), mood);
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

        updateUserStats(user);
        checkMoodChange(user);

        return new OrderResponse(drink, price, user.getBalance(), user.getBarmenMood());
    }

    public MixResponse mixDrinks(String token, String time, List<String> ingredientNames) {
        User user = userStorage.getUserByToken(token);
        if (user == null) {
            throw new UnauthorizedException("User not found");
        }

        String validTime = TimeUtils.extractTime(time);
        boolean isNight = TimeUtils.isNightTime(validTime);
        BarmenMoods mood = user.getBarmenMood();

        List<Ingredient> ingredients;
        try {
            ingredients = ingredientNames.stream()
                    .map(Ingredient::fromRussianName)
                    .toList();
        } catch (IllegalArgumentException e) {
            throw new UnknownRecipeException(user.getBalance(), mood);
        }

        BarmenMoods newMood = checkSecretMix(ingredients, mood, user);
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
            throw new UnknownRecipeException(user.getBalance(), mood);
        }

        DrinkType drink = foundDrink.get();

        if (mood == BarmenMoods.HOSTILE && isComplexDrink(drink)) {
            throw new UnknownRecipeException(user.getBalance(), mood);
        }

        if (isNight && !drink.isNightOnly()) {
            throw new UnknownRecipeException(user.getBalance(), mood);
        }
        if (!isNight && drink.isNightOnly()) {
            throw new UnknownRecipeException(user.getBalance(), mood);
        }

        DrinkType favorite = barStorage.getFavoriteDrink(token);
        boolean isFavorite = (drink == favorite);
        int price = drink.getPrice(mood, isFavorite);

        if (user.getBalance() < price) {
            throw new InsufficientFundsException(price, user.getBalance(), mood);
        }

        barStorage.makeOrder(token, drink, mood, "mix", isNight, isFavorite);
        user.setBalance(user.getBalance() - price);
        updateUserStats(user);
        checkMoodChange(user);

        return new MixResponse(drink, price, user.getBalance(), user.getBarmenMood());
    }

    private BarmenMoods checkSecretMix(List<Ingredient> ingredients, BarmenMoods currentMood, User user) {
        Set<Ingredient> set = new HashSet<>(ingredients);

        // Воздух — ничего не делает
        if (set.isEmpty()) {
            return null; // Просто бесплатный напиток
        }

        // Мертвец — удваивает баланс
        if (set.equals(Set.of(Ingredient.VODKA, Ingredient.RUM, Ingredient.MILK))) {
            user.setBalance(user.getBalance() * 2);
            return currentMood;
        }

        // Ошибка бармена — макс настроение
        if (set.equals(Set.of(Ingredient.TEQUILA, Ingredient.ICE, Ingredient.MILK))) {
            return BarmenMoods.GENEROUS;
        }

        // Зелье бармена — разблокирует секреты
        if (set.equals(Set.of(Ingredient.GIN, Ingredient.JUICE, Ingredient.TONIC, Ingredient.ICE))) {
            user.setSecretUnlocked(true);
            return currentMood;
        }

        // Армагеддон — hostile
        if (set.equals(Set.of(Ingredient.VODKA, Ingredient.RUM, Ingredient.TEQUILA,
                Ingredient.WHISKY, Ingredient.GIN))) {
            return BarmenMoods.HOSTILE;
        }

        return null;
    }

    private DrinkType findDrinkByIngredients(List<Ingredient> ingredients) {
        var found = RecipeMenu.findByIngredients(ingredients);
        return found.orElse(DrinkType.RUSSIAN);
    }

    private boolean isComplexDrink(DrinkType drink) {
        return drink == DrinkType.LONG_ISLAND || drink == DrinkType.WHITE_RUSSIAN;
    }

    private void updateUserStats(User user) {
        Set<DrinkType> uniqueDrinks = barStorage.getUniqueDrinks(user.getToken());
        Rank newRank = Rank.fromUniqueDrinks(uniqueDrinks.size());
        user.setRank(newRank);
    }

    private void checkMoodChange(User user) {
        int totalOrders = barStorage.getTotalOrders(user.getToken());

        if (totalOrders > 0 && totalOrders % 10 == 0) {
            user.setBarmenMood(user.getBarmenMood().shift(1));
        }
    }
}