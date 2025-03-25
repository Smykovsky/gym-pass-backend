package pl.smyk.gympassbackend.training.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TrainingType {
    CARDIO("Kardio"),
    STRENGTH("Siłowy"),
    CALISTHENICS("Kalistenika"),
    CROSSFIT("Crossfit"),
    YOGA("Joga"),
    PILATES("Pilates"),
    HIIT("Trening interwałowy"),
    FUNCTIONAL("Funkcjonalny"),
    MARTIAL_ARTS("Sztuki walki"),
    SPINNING("Spinning");

    private final String value;

    public String getValue() {
        return value;
    }
}