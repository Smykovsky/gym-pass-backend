package pl.smyk.gympassbackend.training.model;

import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;
import pl.smyk.gympassbackend.auth.model.Trainer;
import pl.smyk.gympassbackend.auth.model.User;

import java.time.LocalDateTime;

@Document("scheduled_trainings")
@TypeAlias("scheduledTraining")
@SuperBuilder
@Getter
@Setter
public class ScheduledTraining {
    @MongoId
    private String id;
    private LocalDateTime trainingDateTime;
    @DBRef
    private Trainer trainer;
    @DBRef
    private User user;
    private TrainingStatus trainingStatus;
}
