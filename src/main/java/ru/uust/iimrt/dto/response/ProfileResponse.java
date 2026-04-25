package ru.uust.iimrt.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProfileResponse {

    String id;

    String rank;

    long total_orders;

    long unique_drinks;

    Drink favorite_drink;

    boolean bar_closed;
}
