package ru.uust.iimrt.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateResponse {
    private String token;

    private String id;


}
