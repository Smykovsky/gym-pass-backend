package pl.smyk.gympassbackend.auth.model;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.TypeAlias;
import pl.smyk.gympassbackend.training.model.ScheduledTraining;
import pl.smyk.gympassbackend.training.model.TrainingStatus;
import pl.smyk.gympassbackend.training.model.TrainingType;

import java.util.List;

@TypeAlias("trainer")
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class Trainer extends User {
    @Nullable
    private List<String> certifications;
    @Nullable
    private List<TrainingType> trainingTypes;
    @Nullable
    private List<ScheduledTraining> scheduledTrainings;
}
