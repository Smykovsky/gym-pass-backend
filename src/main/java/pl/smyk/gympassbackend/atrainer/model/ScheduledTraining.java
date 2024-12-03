package pl.smyk.gympassbackend.atrainer.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.LocalDateTime;

@Document("scheduled_trainings")
@TypeAlias("scheduledTraining")
@SuperBuilder
@Getter
@Setter
public class ScheduledTraining {
    @MongoId
    private String id;
    @DBRef
    private Trainer trainer;
    private LocalDateTime trainingDateTime;
    private String firstName;
    private String lastName;
}
