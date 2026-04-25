package ru.uust.iimrt.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor
public class FinanceService {

    @GetMapping("/history")
    @ResponseStatus(HttpStatus.OK)
    public User getHistory(@RequestHeader("Authorization") String authorization) {
        return userService.getHistory(authorization);
    }

    @GetMapping("/balance")
    @ResponseStatus(HttpStatus.OK)
    public User getBalance(@RequestHeader("Authorization") String authorization) {
        return userService.getBalance(authorization);
    }

    @PostMapping("/tip")
    @ResponseStatus(HttpStatus.OK)
    public Bar tipBarmen(
            @RequestHeader("Authorization") String authorization,
            @RequestBody Map<String, List<String>> request) {
        List<String> amount = request.get("amount");
        return BarService.tipBarmen(authorization, amount);
    }

}
