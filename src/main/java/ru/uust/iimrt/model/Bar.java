package ru.uust.iimrt.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Bar {

    private boolean isBarClosed;

    private BarmenMoods barmenMood;
}
