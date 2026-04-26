package ru.uust.iimrt.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import ru.uust.iimrt.model.DrinkType;
import ru.uust.iimrt.model.BarmenMoods;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderResponse {
    private String status;
    private String error;
    private String drink;      // Название на русском
    private Integer price;
    private Integer balance;
    private String mood_level;

    // Успешный ответ
    public OrderResponse(DrinkType drink, int price, int balance, BarmenMoods mood) {
        this.status = "ok";
        this.drink = drink.getRussianName();
        this.price = price;
        this.balance = balance;
        this.mood_level = mood.toString();
    }

    // Ошибка
    public OrderResponse(String error, Integer price, int balance, BarmenMoods mood) {
        this.status = "error";
        this.error = error;
        this.price = price;
        this.balance = balance;
        this.mood_level = mood.toString();
    }

    // Статический фабричный метод для ошибок
    public static OrderResponse error(String error, int balance, BarmenMoods mood) {
        return new OrderResponse(error, null, balance, mood);
    }

    public static OrderResponse error(String error, int price, int balance, BarmenMoods mood) {
        return new OrderResponse(error, price, balance, mood);
    }
}