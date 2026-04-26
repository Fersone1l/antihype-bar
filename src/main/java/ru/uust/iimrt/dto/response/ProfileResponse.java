package ru.uust.iimrt.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.uust.iimrt.model.DrinkType;

@Data
@AllArgsConstructor
public class ProfileResponse {

    String id;

    String rank;

    long total_orders;

    long unique_drinks;

    DrinkType favorite_drink;

    boolean bar_closed;
}
