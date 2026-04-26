package ru.uust.iimrt.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.uust.iimrt.model.Order;
import java.util.List;

@Data
@AllArgsConstructor
public class HistoryResponse {
    List<Order> orders;

    int balance;

    String mood_level;
}
