package pl.smyk.gympassbackend.member.dto;

import lombok.Data;

@Data
public class PaymentConfirmRequest {
    private String paymentId;
    private String blikCode;
}
