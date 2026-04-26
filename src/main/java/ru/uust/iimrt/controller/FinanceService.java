package ru.uust.iimrt.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.uust.iimrt.dto.response.BalanceResponse;
import ru.uust.iimrt.dto.response.HistoryResponse;
import ru.uust.iimrt.dto.response.TipResponse;
import ru.uust.iimrt.service.PaymentService;

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
        return null;
    }

    @GetMapping("/balance")
    @ResponseStatus(HttpStatus.OK)
    public BalanceResponse getBalance(@RequestHeader("Authorization") String authorization) {
        return paymentService.getBalance(authorization);
    }

    @PostMapping("/tip")
    @ResponseStatus(HttpStatus.OK)
    public TipResponse tipBarmen(
            @RequestHeader("Authorization") String authorization,
            @RequestBody Map<String, List<String>> request) {
        List<String> amount = request.get("amount");
        return paymentService.tipBarmen(authorization, amount.get(0));
    }

}
