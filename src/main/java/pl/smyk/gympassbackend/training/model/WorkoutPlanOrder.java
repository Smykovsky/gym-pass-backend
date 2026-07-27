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

import java.time.LocalDateTime;

@Document("workout_plan_orders")
@TypeAlias("workoutPlanOrder")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WorkoutPlanOrder {
    @MongoId
    private String id;

    @DBRef
    private User user;

    @DBRef
    private Trainer trainer;

    private Double price;
    private OrderStatus status;

    private WorkoutPlanQuestionnaire questionnaire;

    @DBRef
    private WorkoutPlan workoutPlan;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
