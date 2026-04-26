package ru.uust.iimrt.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import ru.uust.iimrt.storage.UserStorage;
import ru.uust.iimrt.util.TokenUtils;

import java.io.IOException;

@Component
public class AuthFilter implements Filter {

    private final UserStorage userStorage;

    // Список эндпоинтов, не требующих авторизации
    private static final String[] PUBLIC_ENDPOINTS = {"/register"};

    public AuthFilter(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String path = httpRequest.getRequestURI();

        // Пропускаем публичные эндпоинты
        for (String publicPath : PUBLIC_ENDPOINTS) {
            if (path.equals(publicPath)) {
                chain.doFilter(request, response);
                return;
            }
        }

        // Проверяем авторизацию
        String authHeader = httpRequest.getHeader("Authorization");
        String token = TokenUtils.extractToken(authHeader);

        if (token == null || !userStorage.containsUserToken(token)) {
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        chain.doFilter(request, response);
    }
}