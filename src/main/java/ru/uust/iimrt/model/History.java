package ru.uust.iimrt.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
public class History {
    private long totalOrders;

    private Set<DrinkType> uniqueDrinks;

    private DrinkType favorite_drink;
}
