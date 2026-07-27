package pl.smyk.gympassbackend.training.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.smyk.gympassbackend.training.dto.WorkoutPlanDto;
import pl.smyk.gympassbackend.training.service.WorkoutPlanService;

import java.util.List;

@RestController
@RequestMapping("/api/user/workout-plans")
@RequiredArgsConstructor
public class UserWorkoutPlanController {
    private final WorkoutPlanService workoutPlanService;

    @GetMapping("/active")
    public ResponseEntity<WorkoutPlanDto> getActiveWorkoutPlan(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }
        WorkoutPlanDto activePlan = workoutPlanService.getUserActiveWorkoutPlan(authentication.getName());
        return ResponseEntity.ok(activePlan);
    }

    @GetMapping("/history")
    public ResponseEntity<List<WorkoutPlanDto>> getWorkoutPlanHistory(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }
        List<WorkoutPlanDto> history = workoutPlanService.getUserWorkoutPlanHistory(authentication.getName());
        return ResponseEntity.ok(history);
    }
}
