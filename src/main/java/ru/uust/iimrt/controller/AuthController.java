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
import ru.uust.iimrt.util.TokenUtils;

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
        String token = TokenUtils.extractToken(authorization);
        return authService.reset(token);
    }

    @GetMapping("/profile")
    @ResponseStatus(HttpStatus.OK)
    public ProfileResponse getProfile(@RequestHeader("Authorization") String authorization) {
        String token = TokenUtils.extractToken(authorization);
        return authService.getProfile(token);
    }
}
