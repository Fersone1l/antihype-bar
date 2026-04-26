package ru.uust.iimrt.storage;

import ru.uust.iimrt.model.*;
import java.util.*;

public interface BarStorage {
    Set<DrinkType> getUniqueDrinks(String token);

    DrinkType getFavoriteDrink(String token);

    int getTotalOrders(String token);

    Order makeOrder(String token,
                    DrinkType drink,
                    BarmenMoods mood,
                    boolean isNight,
                    boolean isFavorite);

    Order makeOrder(String token,
                    DrinkType drink,
                    BarmenMoods mood,
                    String method,
                    boolean isNight,
                    boolean isFavorite);

    List<Order> getOrderHistory(String token);
}