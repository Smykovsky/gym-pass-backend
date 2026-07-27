package pl.smyk.gympassbackend.training.dto;

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
public class WorkoutDayDto {
    private String dayName;
    private Integer dayOrder;
    @Builder.Default
    private List<WorkoutExerciseDto> exercises = new ArrayList<>();
}
