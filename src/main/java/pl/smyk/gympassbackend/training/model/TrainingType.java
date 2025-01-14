package pl.smyk.gympassbackend.training.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TrainingType {
    CARDIO,
    STRENGTH,
    CALISTHENICS,
    CROSSFIT,
    YOGA,
    PILATES,
    HIIT,
    FUNCTIONAL,
    MARTIAL_ARTS,
    SPINNING;

    private TrainingType trainingType;
}
