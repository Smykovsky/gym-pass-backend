package pl.smyk.gympassbackend.member.dto;

import lombok.Data;
import pl.smyk.gympassbackend.member.model.MembershipType;

import java.time.LocalDate;

@Data
public class MembershipDto {
    private String id;
    private boolean active;
    private LocalDate startDate;
    private LocalDate endDate;
    private String qrCode;
    private MembershipType membershipType;
}
