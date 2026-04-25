package ru.uust.iimrt.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.uust.iimrt.dto.response.CreateResponse;
import ru.uust.iimrt.dto.response.ProfileResponse;
import ru.uust.iimrt.model.User;
import ru.uust.iimrt.storage.UserStorage;

@AllArgsConstructor
@Service
public class AuthService {
    private final UserStorage userStorage;

    CreateResponse create() {
        return null;
    }

    ProfileResponse getProfile(String authorization) {
        return null;
    }

}
