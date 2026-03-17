package github.hqn03.auth_service.auth.controller;

import github.hqn03.auth_service.auth.dto.auth.LoginRequest;
import github.hqn03.auth_service.auth.dto.auth.LoginResponse;
import github.hqn03.auth_service.auth.dto.auth.RegisterRequest;
import github.hqn03.auth_service.auth.dto.auth.RegisterResponse;
import github.hqn03.auth_service.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest registerRequest) {
        String result = authService.register(registerRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest, @RequestHeader(value = "X-Session-ID", required = false) String sessionId) {
        String result = authService.login(loginRequest, sessionId);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/verify")
    public ResponseEntity<String> verify(@RequestParam("token") String token){
        String result = authService.verifyToken(token);
        return ResponseEntity.ok(result);
    }


}
