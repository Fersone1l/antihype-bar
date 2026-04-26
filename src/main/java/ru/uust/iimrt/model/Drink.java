package ru.uust.iimrt.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Drink {

    String name;

    int price;

    boolean isSecret;

    String effect;
}
