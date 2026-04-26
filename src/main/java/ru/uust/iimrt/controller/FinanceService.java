package ru.uust.iimrt.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.uust.iimrt.dto.response.BalanceResponse;
import ru.uust.iimrt.dto.response.HistoryResponse;
import ru.uust.iimrt.dto.response.TipResponse;
import ru.uust.iimrt.service.PaymentService;
import ru.uust.iimrt.util.TokenUtils;

import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor
public class FinanceService {
    private final PaymentService paymentService;

    @GetMapping("/history")
    @ResponseStatus(HttpStatus.OK)
    public HistoryResponse getHistory(@RequestHeader("Authorization") String authorization) {
        String token = TokenUtils.extractToken(authorization);
        return null;
    }

    @GetMapping("/balance")
    @ResponseStatus(HttpStatus.OK)
    public BalanceResponse getBalance(@RequestHeader("Authorization") String authorization) {
        String token = TokenUtils.extractToken(authorization);
        return paymentService.getBalance(token);
    }

    @PostMapping("/tip")
    @ResponseStatus(HttpStatus.OK)
    public TipResponse tipBarmen(
            @RequestHeader("Authorization") String authorization,
            @RequestBody Map<String, List<String>> request) {
        String token = TokenUtils.extractToken(authorization);
        List<String> amount = request.get("amount");
        return paymentService.tipBarmen(token, amount.get(0));
    }

}
