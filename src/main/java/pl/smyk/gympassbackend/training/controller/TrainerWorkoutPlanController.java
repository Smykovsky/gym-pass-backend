package pl.smyk.gympassbackend.training.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pl.smyk.gympassbackend.training.dto.CreateWorkoutPlanRequest;
import pl.smyk.gympassbackend.training.dto.UpdateWorkoutPlanRequest;
import pl.smyk.gympassbackend.training.dto.WorkoutPlanDto;
import pl.smyk.gympassbackend.training.model.WorkoutStatus;
import pl.smyk.gympassbackend.training.service.WorkoutPlanService;

import java.util.List;

@RestController
@RequestMapping("/api/trainer/workout-plans")
@RequiredArgsConstructor
public class TrainerWorkoutPlanController {
    private final WorkoutPlanService workoutPlanService;

    @PostMapping
    public ResponseEntity<WorkoutPlanDto> createWorkoutPlan(
            Authentication authentication,
            @RequestBody CreateWorkoutPlanRequest request
    ) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }
        WorkoutPlanDto created = workoutPlanService.createWorkoutPlan(authentication.getName(), request);
        return ResponseEntity.ok(created);
    }

    @GetMapping
    public ResponseEntity<List<WorkoutPlanDto>> getTrainerWorkoutPlans(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }
        List<WorkoutPlanDto> plans = workoutPlanService.getTrainerWorkoutPlans(authentication.getName());
        return ResponseEntity.ok(plans);
    }

    @GetMapping("/{planId}")
    public ResponseEntity<WorkoutPlanDto> getWorkoutPlanById(
            Authentication authentication,
            @PathVariable String planId
    ) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }
        WorkoutPlanDto plan = workoutPlanService.getWorkoutPlanById(planId);
        return ResponseEntity.ok(plan);
    }

    @PutMapping("/{planId}")
    public ResponseEntity<WorkoutPlanDto> updateWorkoutPlan(
            Authentication authentication,
            @PathVariable String planId,
            @RequestBody UpdateWorkoutPlanRequest request
    ) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }
        WorkoutPlanDto updated = workoutPlanService.updateWorkoutPlan(authentication.getName(), planId, request);
        return ResponseEntity.ok(updated);
    }

    @PostMapping("/{planId}/assign/{userId}")
    public ResponseEntity<WorkoutPlanDto> assignWorkoutPlan(
            Authentication authentication,
            @PathVariable String planId,
            @PathVariable String userId
    ) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }
        WorkoutPlanDto updated = workoutPlanService.assignWorkoutPlan(authentication.getName(), planId, userId);
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/{planId}/status")
    public ResponseEntity<WorkoutPlanDto> updatePlanStatus(
            Authentication authentication,
            @PathVariable String planId,
            @RequestParam WorkoutStatus status
    ) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }
        WorkoutPlanDto updated = workoutPlanService.updatePlanStatus(authentication.getName(), planId, status);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{planId}")
    public ResponseEntity<Void> deleteWorkoutPlan(
            Authentication authentication,
            @PathVariable String planId
    ) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }
        workoutPlanService.deleteWorkoutPlan(authentication.getName(), planId);
        return ResponseEntity.noContent().build();
    }
}
