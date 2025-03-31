package pl.smyk.gympassbackend.member.dto;

import lombok.Data;

@Data
public class AddUserEntry {
    private String userEmail;
    private String membershipCardId;
}
