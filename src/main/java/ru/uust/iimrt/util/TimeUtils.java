package ru.uust.iimrt.util;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class TimeUtils {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    /**
     * Извлекает и валидирует время из заголовка X-Time
     * Если время некорректное или заголовок отсутствует — возвращает текущее время
     */
    public static String extractTime(String timeHeader) {
        if (timeHeader == null || timeHeader.isBlank()) {
            return getCurrentTime();
        }

        try {
            // Пробуем распарсить HH:mm
            LocalTime.parse(timeHeader.trim(), FORMATTER);
            return timeHeader.trim();
        } catch (DateTimeParseException e) {
            // Некорректный формат — возвращаем текущее время
            return getCurrentTime();
        }
    }

    /**
     * Получить текущее время в формате HH:mm
     */
    public static String getCurrentTime() {
        return LocalTime.now().format(FORMATTER);
    }

    /**
     * Проверить, ночное ли время (00:00 - 06:00)
     */
    public static boolean isNightTime(String time) {
        try {
            LocalTime localTime = LocalTime.parse(time, FORMATTER);
            return localTime.getHour() >= 0 && localTime.getHour() < 6;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}