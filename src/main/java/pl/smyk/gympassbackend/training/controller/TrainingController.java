package pl.smyk.gympassbackend.training.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pl.smyk.gympassbackend.training.dto.NewTrainingRequest;
import pl.smyk.gympassbackend.training.dto.ScheduledTrainingDto;
import pl.smyk.gympassbackend.training.model.ScheduledTraining;
import pl.smyk.gympassbackend.training.service.TrainingService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/training")
public class TrainingController {
    private final TrainingService trainingService;


    @GetMapping()
    public ResponseEntity<?> getUserTrainings(Authentication authentication) {
        String userEmail = authentication.getName();
        if (userEmail.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<ScheduledTrainingDto> userTrainings = trainingService.getUserTrainings(userEmail);
        return ResponseEntity.ok(userTrainings);
    }

    @PostMapping()
    public ResponseEntity<?> sendTrainingRequest(Authentication authentication, @RequestBody NewTrainingRequest request) {
        String userEmail = authentication.getName();

        if (userEmail.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        ScheduledTrainingDto scheduledTrainingDto = trainingService.sendTrainingRequest(request.getTrainerEmail(), userEmail);
        return ResponseEntity.ok(scheduledTrainingDto);
    }
}
