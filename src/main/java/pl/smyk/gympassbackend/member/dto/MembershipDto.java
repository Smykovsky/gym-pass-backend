package pl.smyk.gympassbackend.member.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class MembershipDto {
    private String id;
    private boolean active;
    private LocalDate startDate;
    private LocalDate endDate;
    private String qrCode;
    private String membershipType;
    private String membershipPlanName;
}
