package ru.uust.iimrt.storage.memory;

import org.springframework.stereotype.Component;
import ru.uust.iimrt.model.*;
import ru.uust.iimrt.storage.BarStorage;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class InMemoryBarStorage implements BarStorage {
    private final Map<String, List<Order>> orderHistory = new HashMap<>();

    @Override
    public Set<DrinkType> getUniqueDrinks(String token) {
        List<Order> orders = orderHistory.getOrDefault(token, List.of());
        return orders.stream()
                .map(Order::getDrink)
                .collect(Collectors.toSet());
    }

    @Override
    public DrinkType getFavoriteDrink(String token) {
        List<Order> orders = orderHistory.getOrDefault(token, List.of());
        if (orders.isEmpty()) return null;

        // Считаем количество заказов каждого напитка
        Map<DrinkType, Long> count = orders.stream()
                .collect(Collectors.groupingBy(Order::getDrink, Collectors.counting()));

        // Возвращаем самый популярный
        return count.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    @Override
    public int getTotalOrders(String token) {
        return orderHistory.getOrDefault(token, List.of()).size();
    }

    @Override
    public Order makeOrder(String token,
                           DrinkType drink,
                           BarmenMoods mood,
                           boolean isNight,
                           boolean isFavorite) {
        // Получаем цену с учетом всех факторов
        int price = drink.getPrice(mood, isFavorite);

        Order order = new Order(drink, price, "order");
        orderHistory.computeIfAbsent(token, k -> new ArrayList<>()).add(order);
        return order;
    }

    @Override
    public Order makeOrder(String token,
                           DrinkType drink,
                           BarmenMoods mood,
                           String method,
                           boolean isNight,
                           boolean isFavorite) {
        int price = drink.getPrice(mood, isFavorite);

        Order order = new Order(drink, price, method);
        orderHistory.computeIfAbsent(token, k -> new ArrayList<>()).add(order);
        return order;
    }

    @Override
    public List<Order> getOrderHistory(String token) {
        return orderHistory.getOrDefault(token, List.of());
    }
}