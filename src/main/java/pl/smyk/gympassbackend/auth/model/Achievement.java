package pl.smyk.gympassbackend.auth.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Achievement {
    FIRST_LOGIN("FIRST_LOGIN", "Pierwsze logowanie", 1, false),
    HUNDRED_LOGINS("HUNDRED_LOGINS", "Setne logowanie", 100, false),
    FIRST_TRAINING("FIRST_TRAINING", "Pierwszy trening", 1, false),
    TEN_TRAININGS("TEN_TRAINING", "Dziesięć treningów", 10, false);

    private final String value;
    private final String title;
    private final int requiredProgress;
    private final boolean completed;
}
