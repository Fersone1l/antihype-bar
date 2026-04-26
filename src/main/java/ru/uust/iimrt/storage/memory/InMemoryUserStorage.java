package ru.uust.iimrt.storage.memory;

import org.springframework.stereotype.Component;
import ru.uust.iimrt.dto.response.CreateResponse;
import ru.uust.iimrt.dto.response.ProfileResponse;
import ru.uust.iimrt.dto.response.ResetResponse;
import ru.uust.iimrt.model.BarmenMoods;
import ru.uust.iimrt.model.Rank;
import ru.uust.iimrt.model.User;
import ru.uust.iimrt.storage.UserStorage;

import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<String, User> users = new HashMap<>();
    private static long usersCount = 0;

    @Override
    public ProfileResponse profile(String token) {
        return null;
    }

    @Override
    public CreateResponse create() {
        usersCount++;
        String strId = String.format("BAR-%04d", usersCount);
        String token = generateMD5(strId);

        users.put(token, new User(token,
                Rank.BEGGINER,
                100,
                false,
                BarmenMoods.NORMAL)
        );

        return new CreateResponse("ok", strId, token);
    }

    @Override
    public ResetResponse reset(String token) {
        return new ResetResponse("ok");
    }

//    @Override
//    public ProfileResponse profile(String token) {
//        User user = users.get(token);
//        return new ProfileResponse(user.getToken(),
//                UserToStringRank(user),
//                user.getTotalOrders(),
//                user.getUniqueDrinks().size(),
//                user.getFavorite_drink(),
//                );
//    }

    @Override
    public Boolean containsUserToken(String token) {
        return null;
    }

    @Override
    public User getUserByToken(String token) {
        return users.get(token);
    }

    private static String generateMD5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hash = md.digest(input.getBytes());

            // Конвертируем в hex строку
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

    private String UserToStringRank(User user) {
        Rank rank = user.getRank();

        switch (rank) {
            case BEGGINER -> {
                return "Новичок";
            }
            default -> {
                throw new RuntimeException("Error");
            }
        }
    }
}
