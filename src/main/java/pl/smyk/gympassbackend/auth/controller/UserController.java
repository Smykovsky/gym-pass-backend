package pl.smyk.gympassbackend.auth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.smyk.gympassbackend.auth.dto.request.UpdateUserDataRequest;
import pl.smyk.gympassbackend.auth.dto.response.UserDataResponse;
import pl.smyk.gympassbackend.auth.service.AuthService;
import pl.smyk.gympassbackend.auth.service.UserService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final AuthService authService;
    private final UserService userService;

    @GetMapping()
    public ResponseEntity<?> getUserData(Authentication authentication) {
        if (!authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }

        var email = authentication.getName();

        if (email.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        UserDataResponse userData = authService.getUserData(email);
        return ResponseEntity.ok(userData);
    }

    @PutMapping()
    public ResponseEntity<?> updateUserData(Authentication authentication, @RequestBody UpdateUserDataRequest updateUserDataRequest) {
        Map<String, String> response = new HashMap<>();
        if (!authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }

        if (updateUserDataRequest == null) {
            response.put("message", "Proszę uzupełnić dane!");
            return ResponseEntity.badRequest().build();
        }
        System.out.println(updateUserDataRequest);

        authService.updateUserData(updateUserDataRequest);

        response.put("message", "Pomyślnie zaktualizowano użytkownika!");

        return ResponseEntity.ok(response);
    }

    @PostMapping("/upload-profile-image")
    public ResponseEntity<?> uploadProfileImage(
            Authentication authentication,
            @RequestParam("file") MultipartFile file
    ) {
        try {
            userService.uploadProfileImage(authentication.getName(), file);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

}
