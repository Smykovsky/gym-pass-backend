package pl.smyk.gympassbackend.training.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class NewTrainingRequest {
    private String trainerEmail;
}
