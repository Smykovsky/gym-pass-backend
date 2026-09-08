package pl.smyk.gympassbackend.member.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;

@Document(collection = "membership_plans")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MembershipPlan {

    @MongoId
    private String id;

    @Indexed(unique = true)
    private String code;
    private String name;
    private int months;
    private BigDecimal basePrice;
    private int discountPercent;
    @Builder.Default
    private boolean active = true;
    @Builder.Default
    private int sortOrder = 0;
    private Promotion promotion;
    private Instant createdAt;
    private Instant updatedAt;

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Promotion {
        private boolean enabled;
        private String label;
        private int discountPercent;
        private Instant startsAt;
        private Instant endsAt;
    }

    public boolean isPromotionActive() {
        if (promotion == null || !promotion.isEnabled()) {
            return false;
        }
        Instant now = Instant.now();
        if (promotion.getStartsAt() != null && now.isBefore(promotion.getStartsAt())) {
            return false;
        }
        return promotion.getEndsAt() == null || !now.isAfter(promotion.getEndsAt());
    }

    public int effectiveDiscountPercent() {
        int total = discountPercent + (isPromotionActive() ? promotion.getDiscountPercent() : 0);
        return Math.max(0, Math.min(90, total));
    }

    public BigDecimal finalPrice() {
        BigDecimal base = basePrice != null ? basePrice : BigDecimal.ZERO;
        return base
                .multiply(BigDecimal.valueOf(100 - effectiveDiscountPercent()))
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
    }
}
