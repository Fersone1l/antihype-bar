package ru.uust.iimrt.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.uust.iimrt.dto.response.BalanceResponse;
import ru.uust.iimrt.dto.response.TipResponse;
import ru.uust.iimrt.model.BarmenMoods;
import ru.uust.iimrt.model.User;
import ru.uust.iimrt.storage.UserStorage;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final UserStorage userStorage;

    public BalanceResponse getBalance(String token) {
        User user = userStorage.getUserByToken(token);
        return new BalanceResponse(user.getBalance(), user.getBarmenMood().toString());
    }

    public TipResponse tipBarmen(String token, String amountStr) {
        int amount = Integer.parseInt(amountStr);
        User user = userStorage.getUserByToken(token);

        BarmenMoods mood = user.getBarmenMood();
        int tipModifier = mood.getTipModifier();
        int actualTip = amount + tipModifier;

        // Обновляем баланс
        user.setBalance(user.getBalance() - amount + tipModifier);

        // Чаевые улучшают настроение
        if (amount >= 5) {
            user.setBarmenMood(mood.shift(1));
        }

        return new TipResponse(actualTip, user.getBalance(), user.getBarmenMood().toString());
    }
}