package ru.uust.iimrt.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BalanceResponse {

    int balance;

    String mood_level;
}
