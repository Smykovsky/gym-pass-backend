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
public class UpdateTrainerProfileRequest {
    private String firstName;
    private String lastName;
    private Gender gender;
    private List<String> certifications;
    private List<TrainingType> trainingTypes;
    private String profileImage;
}
