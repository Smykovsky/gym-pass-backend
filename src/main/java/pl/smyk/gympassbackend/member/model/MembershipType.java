package pl.smyk.gympassbackend.member.model;

import lombok.Getter;

@Getter
public enum MembershipType {
    MONTHLY("Miesięczny", 1, 0),
    QUARTERLY("Kwartalny", 3, 10),
    SEMI_ANNUAL("Pół roczny", 6, 20),
    ANNUAL("Roczny",12, 35);

    private static final double PRICE_PER_MONTH = 80.00;
    private final String value;
    private final int months;
    private final int discountPercent ;

    MembershipType(String value, int months, int discountPercent) {
        this.value = value;
        this.months = months;
        this.discountPercent = discountPercent;
    }

    public String getValue() {
        return value;
    }

    public double getDiscountedPrice() {
        double normalPrice = PRICE_PER_MONTH * months;
        double discountAmount = (normalPrice * discountPercent) / 100.0;

        return normalPrice - discountAmount;
    }

}
