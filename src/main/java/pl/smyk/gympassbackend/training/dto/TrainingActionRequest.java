package pl.smyk.gympassbackend.training.dto;

import jakarta.annotation.Nullable;
import lombok.Data;

@Data
public class TrainingActionRequest {
    private String trainingId;
    private String trainerEmail;
    @Nullable
    private String rejectionReason;
}
