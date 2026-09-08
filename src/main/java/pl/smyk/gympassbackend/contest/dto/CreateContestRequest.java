package pl.smyk.gympassbackend.contest.dto;

import lombok.Data;

@Data
public class CreateContestRequest {
    private String title;
    private String description;
    /** Code of the membership plan awarded as the prize (e.g. QUARTERLY). */
    private String membershipType;
    private Integer durationDays; // domyślnie 7 dni
}
