package ru.uust.iimrt.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"status", "tip", "balance", "mood_level"})
public class TipResponse {
    private String status = "ok";
    private int tip;
    private int balance;
    private String mood_level;

    public TipResponse(int tip, int balance, String moodLevel) {
        this.tip = tip;
        this.balance = balance;
        this.mood_level = moodLevel;
    }
}