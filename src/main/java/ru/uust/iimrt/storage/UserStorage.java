package ru.uust.iimrt.storage;

import ru.uust.iimrt.dto.response.CreateResponse;
import ru.uust.iimrt.dto.response.ProfileResponse;
import ru.uust.iimrt.model.User;

public interface UserStorage {

    CreateResponse create();

    void reset(String token);

    ProfileResponse profile(String token);

    Boolean containsUserToken(String token);

    User getUserByToken(String token);

}

