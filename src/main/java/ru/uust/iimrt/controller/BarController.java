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
public class BarController {

    @PostMapping("/order")
    @ResponseStatus(HttpStatus.OK)
    public OrderResponse makeOrder(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("X-Time") String time,
            @RequestBody Drink drink) {
        return BarService.makeOrder(authorization, time, drink);
    }

    @PostMapping("/mix")
    @ResponseStatus(HttpStatus.OK)
    public Bar mixDrinks(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("X-Time") String time,
            @RequestBody Map<String, List<String>> request) {
        List<String> ingredients = request.get("ingredients");
        return BarService.mixDrinks(authorization, time, ingredients);
    }

    @GetMapping("/menu")
    @ResponseStatus(HttpStatus.OK)
    public Bar getMenu(@RequestHeader("Authorization") String authorization,
                       @RequestHeader("X-Time") String time) {
        return BarService.getMenu(authorization, time);
    }


}
