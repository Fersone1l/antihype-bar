package ru.uust.iimrt.storage.memory;

import lombok.Setter;
import org.springframework.stereotype.Component;
import ru.uust.iimrt.dto.response.CreateResponse;
import ru.uust.iimrt.dto.response.ProfileResponse;
import ru.uust.iimrt.dto.response.ResetResponse;
import ru.uust.iimrt.model.BarmenMoods;
import ru.uust.iimrt.model.DrinkType;
import ru.uust.iimrt.model.Rank;
import ru.uust.iimrt.model.User;
import ru.uust.iimrt.storage.BarStorage;
import ru.uust.iimrt.storage.UserStorage;

import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<String, User> users = new HashMap<>();
    private static long usersCount = 0;

    @Setter
    private BarStorage barStorage;

    @Override
    public ProfileResponse profile(String token) {
        User user = users.get(token);
        if (user == null) return null;

        long totalOrders = barStorage != null ? barStorage.getTotalOrders(token) : 0;
        Set<DrinkType> uniqueDrinks = barStorage != null ? barStorage.getUniqueDrinks(token) : Set.of();
        DrinkType favorite = barStorage != null ? barStorage.getFavoriteDrink(token) : null;

        return new ProfileResponse(
                "BAR-" + String.format("%04d", getUserIdByToken(token)),
                user.getRank().getDisplayName(),
                totalOrders,
                uniqueDrinks.size(),
                favorite,
                user.isBarClosed()
        );
    }

    @Override
    public CreateResponse create() {
        usersCount++;
        String strId = String.format("BAR-%04d", usersCount);
        String token = generateMD5(strId);

        users.put(token, new User(token, strId, Rank.BEGINNER, 100, false, BarmenMoods.NORMAL));

        return new CreateResponse("ok", strId, token);
    }

    @Override
    public ResetResponse reset(String token) {
        User user = users.get(token);
        if (user != null) {
            user.setBalance(100);
            user.setRank(Rank.BEGINNER);
            user.setBarClosed(false);
            user.setBarmenMood(BarmenMoods.NORMAL);
            user.setSecretUnlocked(false);
        }
        return new ResetResponse("ok");
    }

    @Override
    public Boolean containsUserToken(String token) {
        return users.containsKey(token);
    }

    @Override
    public User getUserByToken(String token) {
        return users.get(token);
    }

    private long getUserId(String token) {
        // Ищем ID пользователя по токену
        for (Map.Entry<String, User> entry : users.entrySet()) {
            if (entry.getKey().equals(token)) {
                // Извлекаем номер из ID типа "BAR-0001"
                return usersCount; // Заглушка
            }
        }
        return 0;
    }

    private static String generateMD5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hash = md.digest(input.getBytes());

            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xFF & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException("MD5 generation failed", e);
        }
    }

    // Вспомогательный метод
    private long getUserIdByToken(String token) {
        int index = 1;
        for (String key : users.keySet()) {
            if (key.equals(token)) return index;
            index++;
        }
        return 0;
    }



}