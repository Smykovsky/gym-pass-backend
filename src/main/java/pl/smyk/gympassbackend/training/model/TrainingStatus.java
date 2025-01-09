package pl.smyk.gympassbackend.training.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TrainingStatus {
    WAITING, CONFIRMED, DECLINED;

    private TrainingStatus trainingStatus;
}
