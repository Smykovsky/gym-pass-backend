package pl.smyk.gympassbackend.training.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.smyk.gympassbackend.training.model.WorkoutPlanQuestionnaire;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateWorkoutPlanOrderRequest {
    private String trainerId;
    private Double price; // np. 99.00
    private WorkoutPlanQuestionnaire questionnaire;
}
