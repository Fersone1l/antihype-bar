package ru.uust.iimrt.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.uust.iimrt.dto.response.BalanceResponse;
import ru.uust.iimrt.dto.response.HistoryResponse;
import ru.uust.iimrt.dto.response.TipResponse;
import ru.uust.iimrt.model.Order;
import ru.uust.iimrt.model.User;
import ru.uust.iimrt.storage.BarStorage;
import ru.uust.iimrt.storage.UserStorage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class PaymentService {
    private final UserStorage userStorage;
    private final BarStorage barStorage;  // добавить

    // Счётчик чаевых для каждого пользователя
    private final Map<String, Integer> tipTotals = new HashMap<>();

    public BalanceResponse getBalance(String token) {
        User user = userStorage.getUserByToken(token);
        return new BalanceResponse(user.getBalance(), user.getBarmenMood().toString());
    }

    public TipResponse tipBarmen(String token, String amountStr) {
        int amount = Integer.parseInt(amountStr);
        User user = userStorage.getUserByToken(token);

        // Списываем чаевые
        user.setBalance(user.getBalance() - amount);

        // Суммируем чаевые
        int totalTips = tipTotals.getOrDefault(token, 0) + amount;

        // Каждые 20 монет — повышаем настроение на 1 уровень
        while (totalTips >= 20) {
            user.setBarmenMood(user.getBarmenMood().shift(1));
            totalTips -= 20;
        }

        tipTotals.put(token, totalTips);

        return new TipResponse(amount, user.getBalance(), user.getBarmenMood().toString());
    }

    public HistoryResponse getHistory(String token) {
        User user = userStorage.getUserByToken(token);
        List<Order> orders = barStorage.getOrderHistory(token);
        return new HistoryResponse(orders, user.getBalance(), user.getBarmenMood().toString());
    }
}