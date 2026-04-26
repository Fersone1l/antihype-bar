package ru.uust.iimrt.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TipResponse {
    private String status = "ok";
    private int tip;
    private int balance;
    private String mood_level;

    public TipResponse(int tip, int balance, String moodLevel) {
        this.status = "ok";
        this.tip = tip;
        this.balance = balance;
        this.mood_level = moodLevel;
    }
}