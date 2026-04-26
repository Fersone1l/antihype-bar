package ru.uust.iimrt.util;

public class TokenUtils {

    /**
     * Извлекает токен из заголовка Authorization
     * Поддерживает форматы:
     * - "Bearer b45cced982ab91845243e535ff9e9982"
     * - "bearer b45cced982ab91845243e535ff9e9982"
     * - "b45cced982ab91845243e535ff9e9982"
     */
    public static String extractToken(String authorization) {
        if (authorization == null || authorization.isEmpty()) {
            return null;
        }

        String trimmed = authorization.trim();

        // Убираем префикс "Bearer " или "bearer "
        if (trimmed.toLowerCase().startsWith("bearer ")) {
            return trimmed.substring(7).trim();
        }

        return trimmed;
    }
}