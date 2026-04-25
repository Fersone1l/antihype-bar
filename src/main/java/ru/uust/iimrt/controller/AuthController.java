package ru.uust.iimrt.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.uust.iimrt.dto.response.CreateResponse;
import ru.uust.iimrt.storage.UserStorage;

@RestController
@Slf4j
@RequiredArgsConstructor
public class AuthController {

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.OK)
    public CreateResponse createUser() {
        return userService.create();
    }

    @GetMapping("/profile")
    @ResponseStatus(HttpStatus.OK)
    public User getProfile(@RequestHeader("Authorization") String authorization) {
        return userService.getProfile(authorization);
    }

    @PostMapping("/reset")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Void> resetUser(@RequestHeader("Authorization") String authorization) {
        userService.reset();
        return ResponseEntity.ok().build();  // 200 OK, пустое тело
    }


}
