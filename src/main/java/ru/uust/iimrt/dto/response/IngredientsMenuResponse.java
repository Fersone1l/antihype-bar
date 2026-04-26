package ru.uust.iimrt.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.uust.iimrt.model.Ingredient;

import java.util.List;

@Data
public class IngredientsMenuResponse {
    String name;
    int price;
    List<String> ingredients;  // Храним как строки


}