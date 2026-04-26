package ru.uust.iimrt.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.uust.iimrt.dto.response.BalanceResponse;
import ru.uust.iimrt.dto.response.HistoryResponse;
import ru.uust.iimrt.dto.response.TipResponse;
import ru.uust.iimrt.model.User;

@RequiredArgsConstructor
@Service
public class PaymentService {
    private final AuthService authService;

    public BalanceResponse getBalance(String authorization) {
        User user = authService.getUserByToken(authorization);
        return new BalanceResponse(user.getBalance(), user.getBarmenMood().toString());
    }

    public TipResponse tipBarmen(String authorization,
                                 String amountStr) {
        int amount = Integer.parseInt(amountStr);
        User user = authService.getUserByToken(authorization);
        return new TipResponse(amount, user.getBalance(), user.getBarmenMood().toString());
    }

//    public HistoryResponse getHistory(String authorization) {
//        return new HistoryResponse()
//    }
}
