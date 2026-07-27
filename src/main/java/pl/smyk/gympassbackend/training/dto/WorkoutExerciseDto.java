package pl.smyk.gympassbackend.training.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WorkoutExerciseDto {
    private String exerciseName;
    private String muscleGroup;
    private Integer sets;
    private String reps;
    private String targetWeight;
    private Integer restTimeSeconds;
    private String trainerNotes;
}
