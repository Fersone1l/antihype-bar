package ru.uust.iimrt.config;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;
import ru.uust.iimrt.storage.memory.InMemoryBarStorage;
import ru.uust.iimrt.storage.memory.InMemoryUserStorage;

@Configuration
public class StorageConfig {

    private final InMemoryUserStorage userStorage;
    private final InMemoryBarStorage barStorage;

    public StorageConfig(InMemoryUserStorage userStorage, InMemoryBarStorage barStorage) {
        this.userStorage = userStorage;
        this.barStorage = barStorage;
    }

    @PostConstruct
    public void init() {
        userStorage.setBarStorage(barStorage);
    }
}