package pl.smyk.gympassbackend.training.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.smyk.gympassbackend.training.model.WorkoutLevel;
import pl.smyk.gympassbackend.training.model.WorkoutStatus;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateWorkoutPlanRequest {
    private String title;
    private String description;
    private WorkoutLevel level;
    private WorkoutStatus status;
    private String assignedUserId;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<WorkoutDayDto> days;
}
