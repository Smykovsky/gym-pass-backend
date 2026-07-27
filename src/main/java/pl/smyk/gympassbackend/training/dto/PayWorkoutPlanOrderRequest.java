package pl.smyk.gympassbackend.training.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PayWorkoutPlanOrderRequest {
    private String paymentMethod; // CARD, BLIK, TRANSFER
    private String blikCode;      // 6-cyfrowy kod BLIK
}
