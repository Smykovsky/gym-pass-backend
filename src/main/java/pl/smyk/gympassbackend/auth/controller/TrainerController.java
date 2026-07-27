package pl.smyk.gympassbackend.auth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pl.smyk.gympassbackend.auth.dto.request.UpdateTrainerProfileRequest;
import pl.smyk.gympassbackend.auth.dto.TrainerDto;
import pl.smyk.gympassbackend.auth.service.TrainerService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/trainer")
@RequiredArgsConstructor
public class TrainerController {
    private final TrainerService trainerService;

    @GetMapping()
    public ResponseEntity<?> getTrainers(Authentication authentication) {
        if (!authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }

        List<TrainerDto> allTrainers = trainerService.getAllTrainers();
        return ResponseEntity.ok(allTrainers);
    }

    @GetMapping("/me")
    public ResponseEntity<TrainerDto> getMyProfile(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }
        TrainerDto trainer = trainerService.getTrainerProfile(authentication.getName());
        return ResponseEntity.ok(trainer);
    }

    @PutMapping("/me")
    public ResponseEntity<TrainerDto> updateMyProfile(
            Authentication authentication,
            @RequestBody UpdateTrainerProfileRequest request
    ) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }
        TrainerDto updated = trainerService.updateTrainerProfile(authentication.getName(), request);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/{trainerEmail}/availability")
    public ResponseEntity<?> checkTrainerAvailability(Authentication authentication, @PathVariable String trainerEmail, @RequestParam LocalDateTime dateTime) {
        if (!authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }

        boolean trainerAvailable = trainerService.isTrainerAvailable(trainerEmail, dateTime);

        return ResponseEntity.ok(trainerAvailable);
    }
}
