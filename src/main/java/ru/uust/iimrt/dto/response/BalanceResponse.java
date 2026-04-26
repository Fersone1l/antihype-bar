package ru.uust.iimrt.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"status", "balance", "mood_level"})
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