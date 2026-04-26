package ru.uust.iimrt.model;

import lombok.Getter;

@Getter
public enum BarmenMoods {
    HOSTILE("hostile", -2, -40),
    GRUMPY("grumpy", -1, -30),
    NORMAL("normal", 0, 0),
    FRIENDLY("friendly", 1, 10),
    GENEROUS("generous", 2, 30);

    private final String name;
    private final int level;
    private final int tipModifier;

    BarmenMoods(String name, int level, int tipModifier) {
        this.name = name;
        this.level = level;
        this.tipModifier = tipModifier;
    }

    /**
     * Сдвинуть настроение на delta уровней
     * @param delta положительное — добрее, отрицательное — злее
     * @return новое настроение в пределах [-2, 2]
     */
    public BarmenMoods shift(int delta) {
        int newLevel = this.level + delta;
        // Ограничиваем диапазон
        if (newLevel < -2) newLevel = -2;
        if (newLevel > 2) newLevel = 2;

        for (BarmenMoods mood : values()) {
            if (mood.level == newLevel) return mood;
        }
        return this;
    }

    @Override
    public String toString() {
        return name;
    }
}