package pl.smyk.gympassbackend.auth.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.smyk.gympassbackend.auth.model.Gender;
import pl.smyk.gympassbackend.training.model.TrainingType;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateTrainerRequest {
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private Gender gender;
    private List<String> certifications;
    private List<TrainingType> trainingTypes;
}
