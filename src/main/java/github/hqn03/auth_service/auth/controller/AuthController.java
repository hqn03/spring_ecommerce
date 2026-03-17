package github.hqn03.auth_service.auth.controller;

import github.hqn03.auth_service.auth.dto.auth.LoginRequest;
import github.hqn03.auth_service.auth.dto.auth.LoginResponse;
import github.hqn03.auth_service.auth.dto.auth.RegisterRequest;
import github.hqn03.auth_service.auth.dto.auth.RegisterResponse;
import github.hqn03.auth_service.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public RegisterResponse register(@RequestBody RegisterRequest registerRequest) {
        return authService.register(registerRequest);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public LoginResponse login(@RequestBody LoginRequest loginRequest, @RequestHeader(value = "X-Session-ID", required = false) String sessionId) {
        return authService.login(loginRequest, sessionId);
    }


}
