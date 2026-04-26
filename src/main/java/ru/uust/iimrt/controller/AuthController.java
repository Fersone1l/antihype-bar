package ru.uust.iimrt.controller;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.uust.iimrt.dto.response.CreateResponse;
import ru.uust.iimrt.dto.response.ProfileResponse;
import ru.uust.iimrt.dto.response.ResetResponse;
import ru.uust.iimrt.service.AuthService;

@RestController
@Slf4j
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.OK)
    public CreateResponse createUser() {
        return authService.create();
    }


    @PostMapping("/reset")
    @ResponseStatus(HttpStatus.OK)
    public ResetResponse resetUser(@RequestHeader("Authorization") String authorization) {
        authService.reset(authorization);
        return authService.reset(authorization);  // 200 OK, пустое тело
    }

    @GetMapping("/profile")
    @ResponseStatus(HttpStatus.OK)
    public ProfileResponse getProfile(@RequestHeader("Authorization") String authorization) {
        return authService.getProfile(authorization);
    }
}
