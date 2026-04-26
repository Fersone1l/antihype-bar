package ru.uust.iimrt.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.uust.iimrt.dto.request.OrderRequest;
import ru.uust.iimrt.dto.response.MenuResponse;
import ru.uust.iimrt.dto.response.MixResponse;
import ru.uust.iimrt.dto.response.OrderResponse;
import ru.uust.iimrt.service.BarService;

import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor
public class BarController {
    private final BarService barService;

    @PostMapping("/order")
    @ResponseStatus(HttpStatus.OK)
    public OrderResponse makeOrder(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("X-Time") String time,
            @RequestBody OrderRequest request) {
        return barService.makeOrder(authorization, time, request.getName());
    }

//    @PostMapping("/mix")
//    @ResponseStatus(HttpStatus.OK)
//    public MixResponse mixDrinks(
//            @RequestHeader("Authorization") String authorization,
//            @RequestHeader("X-Time") String time,
//            @RequestBody Map<String, List<String>> request) {
//        List<String> ingredients = request.get("ingredients");
//        return BarService.mixDrinks(authorization, time, ingredients);
//    }

    @GetMapping("/menu")
    @ResponseStatus(HttpStatus.OK)
    public MenuResponse getMenu(@RequestHeader("Authorization") String authorization,
                                @RequestHeader("X-Time") String time) {
        return barService.getMenu(authorization, time);
    }


}
