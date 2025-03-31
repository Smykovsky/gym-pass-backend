package pl.smyk.gympassbackend.member.dto;

import lombok.Data;

@Data
public class ValidateMemberResponse {
    private String userEmail;
    private String membershipCardId;
    private boolean isValid;
}
