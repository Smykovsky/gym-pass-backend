package pl.smyk.gympassbackend.member.dto;

import lombok.Data;
import pl.smyk.gympassbackend.member.model.MembershipType;

@Data
public class PaymentCreateRequest {
    private String email;
    private MembershipType membershipType;
}
