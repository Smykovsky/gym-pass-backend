package pl.smyk.gympassbackend.member.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

/** Admin create / update payload for a {@link pl.smyk.gympassbackend.member.model.MembershipPlan}. */
@Data
public class MembershipPlanRequest {
    private String code;
    private String name;
    private Integer months;
    private BigDecimal basePrice;
    private Integer discountPercent;
    private Boolean active;
    private Integer sortOrder;
    private PromotionRequest promotion;

    @Data
    public static class PromotionRequest {
        private Boolean enabled;
        private String label;
        private Integer discountPercent;
        private Instant startsAt;
        private Instant endsAt;
    }
}
