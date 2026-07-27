package pl.smyk.gympassbackend.training.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WorkoutDay {
    private String dayName;
    private Integer dayOrder;
    @Builder.Default
    private List<WorkoutExercise> exercises = new ArrayList<>();
}
