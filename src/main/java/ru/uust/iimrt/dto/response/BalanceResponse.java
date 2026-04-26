package ru.uust.iimrt.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BalanceResponse {
    private String status = "ok";
    private int balance;
    private String mood_level;

    public BalanceResponse(int balance, String moodLevel) {
        this.status = "ok";
        this.balance = balance;
        this.mood_level = moodLevel;
    }
}