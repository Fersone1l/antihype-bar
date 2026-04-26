package ru.uust.iimrt.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.uust.iimrt.dto.response.CreateResponse;
import ru.uust.iimrt.dto.response.ResetResponse;
import ru.uust.iimrt.model.User;
import ru.uust.iimrt.dto.response.ProfileResponse;
import ru.uust.iimrt.storage.UserStorage;

@RequiredArgsConstructor
@Service
public class AuthService {
    private final UserStorage userStorage;

    public CreateResponse create() {
        return userStorage.create();
    }

    public ProfileResponse getProfile(String token) {
        return userStorage.profile(token);
    }

    public ResetResponse reset(String authorization) {
        return userStorage.reset(authorization);
    }

    public User getUserByToken(String authorization) {
        return userStorage.getUserByToken(authorization);
    }

}
