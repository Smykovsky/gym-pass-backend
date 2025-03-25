package pl.smyk.gympassbackend.training.dto;

import lombok.Data;
import pl.smyk.gympassbackend.training.model.TrainingStatus;
import pl.smyk.gympassbackend.training.model.TrainingType;

import java.time.LocalDateTime;

@Data
public class ScheduledTrainingDto {
    private String id;
    private LocalDateTime trainingDateTime;
    private String trainerEmail;
    private String userEmail;
    private String trainingType;
    private String trainingStatus;
    private String rejectionReason;
}
