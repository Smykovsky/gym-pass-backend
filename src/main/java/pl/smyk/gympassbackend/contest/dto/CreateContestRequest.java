package pl.smyk.gympassbackend.contest.dto;

import lombok.Data;
import pl.smyk.gympassbackend.member.model.MembershipType;

@Data
public class CreateContestRequest {
    private String title;
    private String description;
    private MembershipType membershipType;
    private Integer durationDays; // domyślnie 7 dni
}
