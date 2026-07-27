package pl.smyk.gympassbackend.training.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.smyk.gympassbackend.auth.exception.UserNotFoundException;
import pl.smyk.gympassbackend.auth.model.Trainer;
import pl.smyk.gympassbackend.auth.model.User;
import pl.smyk.gympassbackend.auth.repository.TrainerRepository;
import pl.smyk.gympassbackend.auth.repository.UserRepository;
import pl.smyk.gympassbackend.training.dto.CreateWorkoutPlanRequest;
import pl.smyk.gympassbackend.training.dto.UpdateWorkoutPlanRequest;
import pl.smyk.gympassbackend.training.dto.WorkoutPlanDto;
import pl.smyk.gympassbackend.training.mapper.WorkoutPlanMapper;
import pl.smyk.gympassbackend.training.model.WorkoutDay;
import pl.smyk.gympassbackend.training.model.WorkoutPlan;
import pl.smyk.gympassbackend.training.model.WorkoutStatus;
import pl.smyk.gympassbackend.training.repository.WorkoutPlanRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkoutPlanService {
    private final WorkoutPlanRepository workoutPlanRepository;
    private final TrainerRepository trainerRepository;
    private final UserRepository userRepository;

    public WorkoutPlanDto createWorkoutPlan(String trainerEmail, CreateWorkoutPlanRequest request) {
        Trainer trainer = trainerRepository.findByEmail(trainerEmail)
                .orElseThrow(() -> new UserNotFoundException("Nie znaleziono trenera!"));

        User assignedUser = null;
        if (request.getAssignedUserId() != null && !request.getAssignedUserId().isBlank()) {
            assignedUser = userRepository.findById(request.getAssignedUserId())
                    .orElse(null);
        }

        List<WorkoutDay> days = new ArrayList<>();
        if (request.getDays() != null) {
            days = request.getDays().stream()
                    .map(WorkoutPlanMapper::toDayEntity)
                    .collect(Collectors.toList());
        }

        LocalDateTime now = LocalDateTime.now();
        WorkoutPlan plan = WorkoutPlan.builder()
                .trainer(trainer)
                .assignedUser(assignedUser)
                .title(request.getTitle())
                .description(request.getDescription())
                .level(request.getLevel())
                .status(request.getStatus() != null ? request.getStatus() : WorkoutStatus.DRAFT)
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .days(days)
                .createdAt(now)
                .updatedAt(now)
                .build();

        WorkoutPlan saved = workoutPlanRepository.save(plan);
        return WorkoutPlanMapper.toDto(saved);
    }

    public List<WorkoutPlanDto> getTrainerWorkoutPlans(String trainerEmail) {
        Trainer trainer = trainerRepository.findByEmail(trainerEmail)
                .orElseThrow(() -> new UserNotFoundException("Nie znaleziono trenera!"));

        List<WorkoutPlan> plans = workoutPlanRepository.findAllByTrainerIdOrderByCreatedAtDesc(trainer.getId());
        return plans.stream().map(WorkoutPlanMapper::toDto).collect(Collectors.toList());
    }

    public WorkoutPlanDto getWorkoutPlanById(String planId) {
        WorkoutPlan plan = workoutPlanRepository.findById(planId)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono planu treningowego!"));
        return WorkoutPlanMapper.toDto(plan);
    }

    public WorkoutPlanDto updateWorkoutPlan(String trainerEmail, String planId, UpdateWorkoutPlanRequest request) {
        WorkoutPlan plan = workoutPlanRepository.findById(planId)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono planu treningowego!"));

        if (request.getTitle() != null) {
            plan.setTitle(request.getTitle());
        }
        if (request.getDescription() != null) {
            plan.setDescription(request.getDescription());
        }
        if (request.getLevel() != null) {
            plan.setLevel(request.getLevel());
        }
        if (request.getStatus() != null) {
            plan.setStatus(request.getStatus());
        }
        if (request.getStartDate() != null) {
            plan.setStartDate(request.getStartDate());
        }
        if (request.getEndDate() != null) {
            plan.setEndDate(request.getEndDate());
        }

        if (request.getAssignedUserId() != null) {
            if (request.getAssignedUserId().isBlank()) {
                plan.setAssignedUser(null);
            } else {
                User user = userRepository.findById(request.getAssignedUserId()).orElse(null);
                plan.setAssignedUser(user);
            }
        }

        if (request.getDays() != null) {
            List<WorkoutDay> days = request.getDays().stream()
                    .map(WorkoutPlanMapper::toDayEntity)
                    .collect(Collectors.toList());
            plan.setDays(days);
        }

        plan.setUpdatedAt(LocalDateTime.now());
        WorkoutPlan saved = workoutPlanRepository.save(plan);
        return WorkoutPlanMapper.toDto(saved);
    }

    public WorkoutPlanDto assignWorkoutPlan(String trainerEmail, String planId, String userId) {
        WorkoutPlan plan = workoutPlanRepository.findById(planId)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono planu treningowego!"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Nie znaleziono klubowicza!"));

        plan.setAssignedUser(user);
        plan.setStatus(WorkoutStatus.ACTIVE);
        plan.setUpdatedAt(LocalDateTime.now());

        WorkoutPlan saved = workoutPlanRepository.save(plan);
        return WorkoutPlanMapper.toDto(saved);
    }

    public WorkoutPlanDto updatePlanStatus(String trainerEmail, String planId, WorkoutStatus status) {
        WorkoutPlan plan = workoutPlanRepository.findById(planId)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono planu treningowego!"));

        plan.setStatus(status);
        plan.setUpdatedAt(LocalDateTime.now());

        WorkoutPlan saved = workoutPlanRepository.save(plan);
        return WorkoutPlanMapper.toDto(saved);
    }

    public void deleteWorkoutPlan(String trainerEmail, String planId) {
        WorkoutPlan plan = workoutPlanRepository.findById(planId)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono planu treningowego!"));
        workoutPlanRepository.delete(plan);
    }

    public WorkoutPlanDto getUserActiveWorkoutPlan(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("Nie znaleziono użytkownika!"));

        WorkoutPlan plan = workoutPlanRepository
                .findFirstByAssignedUserIdAndStatusOrderByCreatedAtDesc(user.getId(), WorkoutStatus.ACTIVE)
                .orElse(null);

        return WorkoutPlanMapper.toDto(plan);
    }

    public List<WorkoutPlanDto> getUserWorkoutPlanHistory(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("Nie znaleziono użytkownika!"));

        List<WorkoutPlan> plans = workoutPlanRepository.findAllByAssignedUserIdOrderByCreatedAtDesc(user.getId());
        return plans.stream().map(WorkoutPlanMapper::toDto).collect(Collectors.toList());
    }
}
