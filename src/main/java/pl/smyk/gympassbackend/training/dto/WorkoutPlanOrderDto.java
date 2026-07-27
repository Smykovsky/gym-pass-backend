package pl.smyk.gympassbackend.training.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.smyk.gympassbackend.training.model.OrderStatus;
import pl.smyk.gympassbackend.training.model.WorkoutPlanQuestionnaire;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WorkoutPlanOrderDto {
    private String id;
    private String userId;
    private String userEmail;
    private String userName;

    private String trainerId;
    private String trainerEmail;
    private String trainerName;

    private Double price;
    private OrderStatus status;

    private WorkoutPlanQuestionnaire questionnaire;
    private WorkoutPlanDto workoutPlan;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
