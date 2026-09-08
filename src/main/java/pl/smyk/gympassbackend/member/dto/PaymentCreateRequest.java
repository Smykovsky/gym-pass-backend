package pl.smyk.gympassbackend.member.dto;

import lombok.Data;

@Data
public class PaymentCreateRequest {
    private String email;
    /** Code of the {@link pl.smyk.gympassbackend.member.model.MembershipPlan} being purchased. */
    private String membershipType;
}
