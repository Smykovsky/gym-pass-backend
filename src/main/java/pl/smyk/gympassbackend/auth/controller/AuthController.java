package pl.smyk.gympassbackend.auth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    private final AuthService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
        HashMap<String, Object> responseMap = new HashMap<>();
        if (!registerRequest.getPassword().equals(registerRequest.getPasswordConfirmed())) {
            responseMap.put("message", "Hasła muszą być takie same!");
            return ResponseEntity.status(409).body(responseMap);
        }

        if (userService.findByEmail(registerRequest.getEmail()).isPresent()) {
            responseMap.put("message", "Użytkownik o takim adresie email już istnieje!");
            return ResponseEntity.status(409).body(responseMap);
        }

        AuthResponse registerResponse = userService.register(registerRequest);
        responseMap.put("message", registerResponse.getMessage());
        responseMap.put("User", registerRequest.getUsername());
        return ResponseEntity.ok(responseMap);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest authRequest) {
        HashMap<String, Object> responseMap = new HashMap<>();
        Optional<User> userByEmail = userService.findByEmail(authRequest.getEmail());

        if (userByEmail.isEmpty()) {
            responseMap.put("message", "Nie znaleziono użytkownika o takim adresie email!");
            return ResponseEntity.status(401).body(responseMap);
        }

        try {
            AuthResponse tokens = userService.login(authRequest);
            responseMap.put("zalogowano", authRequest.getEmail());
            responseMap.put("tokens", tokens);
            return ResponseEntity.ok(responseMap);
        } catch (Exception e) {
            responseMap.put("message", e.getMessage());
            return ResponseEntity.status(401).body(responseMap);
        }
    }

    @GetMapping("/user/{email}")
    public ResponseEntity<?> getUserData(@PathVariable String email) {
        if (email.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        UserDataResponse userData = userService.getUserData(email);
        return ResponseEntity.ok(userData);
    }
}
