package pl.smyk.gympassbackend.training.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TrainingStatus {
    WAITING("Oczekujący"),
    CONFIRMED("Potwierdzony"),
    DECLINED("Odrzucony");

    private final String value;

    public String getValue() {
        return value;
    }
}
