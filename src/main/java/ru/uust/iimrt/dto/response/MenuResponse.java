package ru.uust.iimrt.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class MenuResponse {
    String status = "ok";
    List<IngredientsMenuResponse> drinks;
    int balance;
    String mood_level;
    String note;  // Добавлено поле для подсказки

}