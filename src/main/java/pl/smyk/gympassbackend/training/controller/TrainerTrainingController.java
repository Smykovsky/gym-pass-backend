package pl.smyk.gympassbackend.training.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pl.smyk.gympassbackend.training.dto.ScheduledTrainingDto;
import pl.smyk.gympassbackend.training.dto.TrainingActionRequest;
import pl.smyk.gympassbackend.training.model.ScheduledTraining;
import pl.smyk.gympassbackend.training.service.TrainerTrainingService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/training/trainer")
@PreAuthorize("hasAuthority('ROLE_TRAINER')")
public class TrainerTrainingController {
    private final TrainerTrainingService trainerTrainingService;

    @GetMapping()
    public ResponseEntity<?> getTrainerTrainings(Authentication authentication) {
        if (!authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }

        String trainerEmail = authentication.getName();

        List<ScheduledTrainingDto> trainerTrainings = trainerTrainingService.getTrainerTrainings(trainerEmail);

        return ResponseEntity.ok(trainerTrainings);
    }

    @PostMapping("/accept")
    public ResponseEntity<?> acceptTrainingRequest(Authentication authentication, @RequestBody TrainingActionRequest request) {
        if (!authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }

        ScheduledTrainingDto scheduledTrainingDto = trainerTrainingService.acceptTrainingRequest(request);
        return ResponseEntity.ok(scheduledTrainingDto);
    }

    @PostMapping("/decline")
    public ResponseEntity<?> declineTrainingRequest(Authentication authentication, @RequestBody TrainingActionRequest request) {
        if (!authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }

        ScheduledTrainingDto scheduledTrainingDto = trainerTrainingService.declineTraining(request);
        return ResponseEntity.ok(scheduledTrainingDto);
    }
}
