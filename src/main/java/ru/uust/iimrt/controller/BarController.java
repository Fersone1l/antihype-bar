package ru.uust.iimrt.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.uust.iimrt.dto.request.OrderRequest;
import ru.uust.iimrt.dto.response.MenuResponse;
import ru.uust.iimrt.dto.response.MixResponse;
import ru.uust.iimrt.dto.response.OrderResponse;
import ru.uust.iimrt.exception.UnknownDrinkException;
import ru.uust.iimrt.exception.UnknownRecipeException;
import ru.uust.iimrt.model.BarmenMoods;
import ru.uust.iimrt.model.DrinkType;
import ru.uust.iimrt.model.User;
import ru.uust.iimrt.service.BarService;
import ru.uust.iimrt.storage.UserStorage;
import ru.uust.iimrt.util.TimeUtils;
import ru.uust.iimrt.util.TokenUtils;

import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor
public class BarController {
    private final BarService barService;
    private final UserStorage userStorage;

    @PostMapping("/order")
    @ResponseStatus(HttpStatus.OK)
    public OrderResponse makeOrder(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader(value = "X-Time", required = false) String time,
            @RequestBody OrderRequest request) {
        String validTime = TimeUtils.extractTime(time);
        String token = TokenUtils.extractToken(authorization);

        DrinkType drink;
        try {
            drink = request.getDrinkType();
        } catch (IllegalArgumentException e) {
            User user = userStorage.getUserByToken(token);
            BarmenMoods mood = user != null ? user.getBarmenMood() : BarmenMoods.NORMAL;
            int balance = user != null ? user.getBalance() : 0;
            throw new UnknownDrinkException(balance, mood);
        }

        return barService.makeOrder(token, validTime, drink);
    }

    @PostMapping("/mix")
    @ResponseStatus(HttpStatus.OK)
    public MixResponse mixDrinks(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader(value = "X-Time", required = false) String time,
            @RequestBody Map<String, List<String>> request) {
        String token = TokenUtils.extractToken(authorization);
        String validTime = TimeUtils.extractTime(time);
        List<String> ingredients = request.get("ingredients");
        return barService.mixDrinks(token, validTime, ingredients);
    }

    @GetMapping("/menu")
    @ResponseStatus(HttpStatus.OK)
    public MenuResponse getMenu(@RequestHeader("Authorization") String authorization,
                                @RequestHeader(value = "X-Time", required = false) String time) {
        String token = TokenUtils.extractToken(authorization);
        String validTime = TimeUtils.extractTime(time);
        return barService.getMenu(token, validTime);
    }
}