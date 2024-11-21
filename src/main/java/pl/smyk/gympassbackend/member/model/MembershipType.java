package pl.smyk.gympassbackend.member.model;

import lombok.Data;
import lombok.Getter;

@Getter
public enum MembershipType {
    MONTHLY(1, 0),
    QUARTERLY(3, 10),
    SEMI_ANNUAL(6, 15),
    ANNUAL(12, 20);

    private static final double PRICE_PER_MONTH = 80.00;
    private final int months;
    private final int discountPercent ;

    MembershipType(int months, int discountPercent) {
        this.months = months;
        this.discountPercent = discountPercent;
    }

    public double getDiscountedPrice() {
        double normalPrice = PRICE_PER_MONTH * months;
        double discountAmount = (normalPrice * discountPercent) / 100.0;

        return normalPrice - discountAmount;
    }

}
