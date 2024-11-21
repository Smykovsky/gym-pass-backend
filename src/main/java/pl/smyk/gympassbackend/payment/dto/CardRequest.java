package pl.smyk.gympassbackend.payment.dto;

import lombok.Data;

@Data
public class CardRequest {
    public String number;
    public String expMonth;
    public String expYear;
    public String cvc;
}
