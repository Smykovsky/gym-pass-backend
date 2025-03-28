package pl.smyk.gympassbackend.auth.dto;

import jakarta.annotation.Nullable;
import lombok.Data;
import pl.smyk.gympassbackend.member.dto.MembershipDto;
import pl.smyk.gympassbackend.training.model.ScheduledTraining;
import pl.smyk.gympassbackend.training.model.TrainingType;

import java.util.List;

@Data
public class TrainerDto {
    private String id;
    private String email;
    private String firstName;
    private String lastName;
    private MembershipDto membership;
    private List<String> certifications;
    private List<TrainingType> trainingTypes;
    private List<ScheduledTraining> scheduledTrainings;
}
