package ru.uust.iimrt.dto.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonPropertyOrder({"status", "id", "token"})
public class CreateResponse {
    String status = "ok";
    private String id;
    private String token;
}
