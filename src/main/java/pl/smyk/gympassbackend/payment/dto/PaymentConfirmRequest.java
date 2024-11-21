package pl.smyk.gympassbackend.payment.dto;

import lombok.Data;

@Data
public class PaymentConfirmRequest {
    private String paymentId;
    private String blikCode;
}
