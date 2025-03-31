package pl.smyk.gympassbackend.auth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pl.smyk.gympassbackend.auth.dto.request.LoginRequest;
import pl.smyk.gympassbackend.auth.dto.request.RegisterRequest;
import pl.smyk.gympassbackend.auth.dto.response.AuthResponse;
import pl.smyk.gympassbackend.auth.dto.response.UserDataResponse;
import pl.smyk.gympassbackend.auth.model.User;
import pl.smyk.gympassbackend.auth.service.AuthService;

import java.util.HashMap;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
        HashMap<String, Object> responseMap = new HashMap<>();
        if (!registerRequest.getPassword().equals(registerRequest.getPasswordConfirmed())) {
            responseMap.put("message", "Hasła muszą być takie same!");
            return ResponseEntity.status(409).body(responseMap);
        }

        if (authService.findByEmail(registerRequest.getEmail()).isPresent()) {
            responseMap.put("message", "Użytkownik o takim adresie email już istnieje!");
            return ResponseEntity.status(409).body(responseMap);
        }

        AuthResponse registerResponse = authService.register(registerRequest);
        responseMap.put("message", registerResponse.getMessage());
        responseMap.put("User", registerRequest.getEmail());
        return ResponseEntity.ok(responseMap);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest authRequest) {
        HashMap<String, Object> responseMap = new HashMap<>();
        Optional<User> userByEmail = authService.findByEmail(authRequest.getEmail());

        if (userByEmail.isEmpty()) {
            responseMap.put("message", "Nie znaleziono użytkownika o takim adresie email!");
            return ResponseEntity.status(401).body(responseMap);
        }

        try {
            AuthResponse tokens = authService.login(authRequest);
            responseMap.put("zalogowano", authRequest.getEmail());
            responseMap.put("tokens", tokens);
            return ResponseEntity.ok(responseMap);
        } catch (Exception e) {
            responseMap.put("message", e.getMessage());
            return ResponseEntity.status(401).body(responseMap);
        }
    }
}
