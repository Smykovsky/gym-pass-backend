package pl.smyk.gympassbackend.member.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Data
public class PaymentResponse {
    private String paymentId;
    private Long amount;
    private Map<String, String> metaData;
    private Long created;
}
