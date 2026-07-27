package pl.smyk.gympassbackend.training.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;
import pl.smyk.gympassbackend.auth.model.Trainer;
import pl.smyk.gympassbackend.auth.model.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document("workout_plans")
@TypeAlias("workoutPlan")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WorkoutPlan {
    @MongoId
    private String id;

    @DBRef
    private Trainer trainer;

    @DBRef
    private User assignedUser;

    private String title;
    private String description;
    private WorkoutLevel level;
    private WorkoutStatus status;

    private LocalDate startDate;
    private LocalDate endDate;

    @Builder.Default
    private List<WorkoutDay> days = new ArrayList<>();

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
