package pl.smyk.gympassbackend.training.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.smyk.gympassbackend.training.model.WorkoutLevel;
import pl.smyk.gympassbackend.training.model.WorkoutStatus;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateWorkoutPlanRequest {
    private String title;
    private String description;
    private WorkoutLevel level;
    private WorkoutStatus status; // DRAFT or ACTIVE
    private String assignedUserId; // optional user ID
    private LocalDate startDate;
    private LocalDate endDate;
    @Builder.Default
    private List<WorkoutDayDto> days = new ArrayList<>();
}
