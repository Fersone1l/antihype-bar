package ru.uust.iimrt.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import ru.uust.iimrt.model.DrinkType;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"status", "id", "rank", "total_orders", "unique_drinks", "favorite_drink", "bar_closed"})
public class ProfileResponse {
    private String status = "ok";
    private String id;
    private String rank;
    private long total_orders;
    private long unique_drinks;
    private String favorite_drink;
    private boolean bar_closed;

    public ProfileResponse(String id, String rank, long totalOrders,
                           long uniqueDrinks, DrinkType favoriteDrink, boolean barClosed) {
        this.id = id;
        this.rank = rank;
        this.total_orders = totalOrders;
        this.unique_drinks = uniqueDrinks;
        this.favorite_drink = favoriteDrink != null ? favoriteDrink.getRussianName() : null;
        this.bar_closed = barClosed;
    }
}