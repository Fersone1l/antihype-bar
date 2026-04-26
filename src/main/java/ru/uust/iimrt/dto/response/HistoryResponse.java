package ru.uust.iimrt.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import ru.uust.iimrt.model.Order;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HistoryResponse {
    private String status = "ok";
    private List<Order> orders;
    private int balance;
    private String mood_level;

    public HistoryResponse(List<Order> orders, int balance, String moodLevel) {
        this.status = "ok";
        this.orders = orders;
        this.balance = balance;
        this.mood_level = moodLevel;
    }
}