package pl.smyk.gympassbackend.training.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WorkoutExercise {
    private String exerciseName;
    private String muscleGroup;
    private Integer sets;
    private String reps;
    private String targetWeight;
    private Integer restTimeSeconds;
    private String trainerNotes;
}
