package ru.uust.iimrt.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import ru.uust.iimrt.model.DrinkType;
import ru.uust.iimrt.model.BarmenMoods;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"status", "error", "drink", "price", "balance", "mood_level"})
public class MixResponse {
    private String status;
    private String error;
    private String drink;
    private Integer price;
    private Integer balance;
    private String mood_level;

    // Успех
    public MixResponse(DrinkType drink, int price, int balance, BarmenMoods mood) {
        this.status = "ok";
        this.drink = drink.getRussianName();
        this.price = price;
        this.balance = balance;
        this.mood_level = mood.toString();
    }

    // Ошибка
    public MixResponse(String error, Integer price, int balance, BarmenMoods mood) {
        this.status = "error";
        this.error = error;
        this.price = price;
        this.balance = balance;
        this.mood_level = mood.toString();
    }

    public static MixResponse error(String error, int balance, BarmenMoods mood) {
        return new MixResponse(error, null, balance, mood);
    }

    public static MixResponse error(String error, int price, int balance, BarmenMoods mood) {
        return new MixResponse(error, price, balance, mood);
    }
}