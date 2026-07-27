package pl.smyk.gympassbackend.training.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WorkoutPlanQuestionnaire {
    private String goal;                     // np. Redukcja tkanki tłuszczowej, budowa siły
    private WorkoutLevel experienceLevel;    // BEGINNER, INTERMEDIATE, ADVANCED
    private Integer daysPerWeek;             // np. 3, 4, 5
    private String preferredEquipment;      // np. Wolne ciężary, maszyny, kalistenika
    private String injuriesAndNotes;         // np. Ból w dolnym odcinku kręgosłupa
}
