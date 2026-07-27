package pl.smyk.gympassbackend.training.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.smyk.gympassbackend.training.model.WorkoutLevel;
import pl.smyk.gympassbackend.training.model.WorkoutStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WorkoutPlanDto {
    private String id;
    private String trainerId;
    private String trainerEmail;
    private String trainerName;
    
    private String assignedUserId;
    private String assignedUserEmail;
    private String assignedUserName;
    
    private String title;
    private String description;
    private WorkoutLevel level;
    private WorkoutStatus status;
    
    private LocalDate startDate;
    private LocalDate endDate;
    
    @Builder.Default
    private List<WorkoutDayDto> days = new ArrayList<>();
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
