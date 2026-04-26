package ru.uust.iimrt.dto.request;

import lombok.Data;
import ru.uust.iimrt.model.DrinkType;

@Data
public class OrderRequest {
    private String name;

    public DrinkType getDrinkType() {
        return DrinkType.fromRussianName(name);
    }
}