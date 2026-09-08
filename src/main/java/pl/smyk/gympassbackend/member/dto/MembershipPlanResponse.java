package pl.smyk.gympassbackend.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.smyk.gympassbackend.member.model.MembershipPlan;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MembershipPlanResponse {
    private String id;
    private String code;
    private String name;
    private int months;
    private BigDecimal basePrice;
    private int discountPercent;
    private int effectiveDiscountPercent;
    private BigDecimal finalPrice;
    private boolean active;
    private int sortOrder;
    private PromotionResponse promotion;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PromotionResponse {
        private boolean enabled;
        private boolean active;
        private String label;
        private int discountPercent;
        private Instant startsAt;
        private Instant endsAt;
    }

    public static MembershipPlanResponse from(MembershipPlan plan) {
        PromotionResponse promo = null;
        if (plan.getPromotion() != null) {
            MembershipPlan.Promotion p = plan.getPromotion();
            promo = PromotionResponse.builder()
                    .enabled(p.isEnabled())
                    .active(plan.isPromotionActive())
                    .label(p.getLabel())
                    .discountPercent(p.getDiscountPercent())
                    .startsAt(p.getStartsAt())
                    .endsAt(p.getEndsAt())
                    .build();
        }

        return MembershipPlanResponse.builder()
                .id(plan.getId())
                .code(plan.getCode())
                .name(plan.getName())
                .months(plan.getMonths())
                .basePrice(plan.getBasePrice())
                .discountPercent(plan.getDiscountPercent())
                .effectiveDiscountPercent(plan.effectiveDiscountPercent())
                .finalPrice(plan.finalPrice())
                .active(plan.isActive())
                .sortOrder(plan.getSortOrder())
                .promotion(promo)
                .build();
    }
}
