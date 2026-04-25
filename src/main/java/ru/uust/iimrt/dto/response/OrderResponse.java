package ru.uust.iimrt.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.uust.iimrt.model.DrinkType;

@Data
@AllArgsConstructor
public class OrderResponse {
    DrinkType drink;


}
