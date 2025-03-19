package pl.smyk.gympassbackend.training.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import pl.smyk.gympassbackend.training.model.TrainingType;

@Getter
@Setter
@Data
public class NewTrainingRequest {
    private String trainerEmail;
    private TrainingType trainingType;
}
