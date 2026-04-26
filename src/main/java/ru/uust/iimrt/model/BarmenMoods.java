package ru.uust.iimrt.model;

import lombok.Getter;

@Getter
public enum BarmenMoods {
    HOSTILE("hostile"),
    GRUMPY("grumpy"),
    NORMAL("normal"),
    FRIENDLY("friendly"),
    GENEROUS("generous");

    private final String name;

    BarmenMoods(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
