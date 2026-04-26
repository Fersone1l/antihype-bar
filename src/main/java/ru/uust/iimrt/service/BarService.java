package ru.uust.iimrt.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.uust.iimrt.dto.response.IngredientsMenuResponse;
import ru.uust.iimrt.dto.response.MenuResponse;
import ru.uust.iimrt.dto.response.OrderResponse;
import ru.uust.iimrt.model.*;
import ru.uust.iimrt.storage.BarStorage;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class BarService {
    private final BarStorage barStorage;
    private final AuthService authService;

    public OrderResponse makeOrder(String token, String time, DrinkType drink) {
        User user = authService.getUserByToken(token);
        BarmenMoods mood = calculateMood(user, time);
        boolean isNight = Menu.isNightTime(time);
        DrinkType favorite = barStorage.getFavoriteDrink(token);
        boolean isFavorite = (drink == favorite);

        // Проверяем, доступен ли напиток сейчас
        if (drink.isNightOnly() && !isNight) {
            //return OrderResponse.error("unknown_drink", user.getBalance(), mood);
            throw new IllegalArgumentException("error");
        }

        int price = drink.getPrice(mood, isFavorite);

        // Проверяем баланс
        if (user.getBalance() < price) {
            throw new IllegalArgumentException("error");
        }

        // Выполняем заказ
        Order order = barStorage.makeOrder(token, drink, mood, isNight, isFavorite);
        user.setBalance(user.getBalance() - price);

        return new OrderResponse(
                drink,
                price,
                user.getBalance(),
                mood.toString()
        );
    }


    public MenuResponse getMenu (String token, String time){
        User user = authService.getUserByToken(token);
        BarmenMoods mood = user.getBarmenMood();
        boolean isNight = Menu.isNightTime(time);
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

    private BarmenMoods calculateMood(User user, String time) {
        BarmenMoods baseMood = user.getBarmenMood();
        int hour = Integer.parseInt(time.split(":")[0]);

        if (hour >= 0 && hour < 6) {
            return shiftMood(baseMood, 1);   // Ночь — добрее
        }
        if (hour >= 6 && hour < 10) {
            return shiftMood(baseMood, -1);  // Утро — злее
        }
        if (hour >= 18 && hour < 22) {
            return shiftMood(baseMood, 1);   // Вечер — добрее
        }
        if (hour >= 22) {
            return shiftMood(baseMood, 2);   // Поздний вечер — самый добрый
        }

        return baseMood;  // День — без изменений
    }

    private BarmenMoods shiftMood(BarmenMoods current, int shift) {
        BarmenMoods[] moods = BarmenMoods.values();
        int newIndex = current.ordinal() + shift;
        if (newIndex < 0) newIndex = 0;
        if (newIndex >= moods.length) newIndex = moods.length - 1;
        return moods[newIndex];
    }
}
