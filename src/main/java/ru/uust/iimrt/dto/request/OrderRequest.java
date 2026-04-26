package ru.uust.iimrt.dto.request;

import lombok.Data;
import lombok.Setter;
import ru.uust.iimrt.model.DrinkType;

@Setter
public class OrderRequest {
    private String name;

    public DrinkType getName() {
        return DrinkType.fromRussianName(name);
    }
}
