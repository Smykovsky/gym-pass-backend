package pl.smyk.gympassbackend.member.service;

import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentConfirmParams;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.smyk.gympassbackend.member.dto.PaymentConfirmRequest;
import pl.smyk.gympassbackend.member.dto.PaymentCreateRequest;

import java.util.*;

import static com.stripe.param.PaymentIntentConfirmParams.PaymentMethodData.Type.BLIK;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final MembershipService membershipService;

    private int RETRY_COUNT = 0;
    private final int MAX_RETRY_COUNT = 12;
    private final int DELAY_MILLIS = 2000;

    public PaymentIntent findPaymentById(String paymentId) throws StripeException {
        PaymentIntent retrieve = PaymentIntent.retrieve(paymentId);
        if (retrieve == null) {
            return null;
        }

        return retrieve;
    }

//    public String createCardToken(CardRequest cardRequest) throws StripeException {
//        Map<String, Object> card = new HashMap<>();
//        card.put("number", cardRequest.getNumber());
//        card.put("exp_month", cardRequest.getExpMonth());
//        card.put("exp_year", cardRequest.getExpYear());
//        card.put("cvc", cardRequest.getCvc());
//
//        Map<String, Object> params = new HashMap<>();
//        params.put("card", card);
//        return Token.create(params).getId();
//    }

    public PaymentIntent createPayment(PaymentCreateRequest request) throws StripeException {
        Map<String, Object> params = new HashMap<>();
        params.put("amount", (int) (request.getMembershipType().getDiscountedPrice() * 100));
        params.put("currency", "PLN");
        params.put("payment_method_types", List.of("blik", "card"));
        params.put("confirmation_method", "manual");

        Map<String, Object> metadata = new HashMap<>();
        metadata.put("user_email", request.getEmail());
        metadata.put("membership_type", request.getMembershipType());
        metadata.put("membership_type_months", request.getMembershipType().getMonths());

        params.put("metadata", metadata);

        return PaymentIntent.create(params);
    }

    public String confirmPayment(PaymentConfirmRequest paymentConfirmRequest) {
        try {
            PaymentIntent paymentById = findPaymentById(paymentConfirmRequest.getPaymentId());
            PaymentIntentConfirmParams params = PaymentIntentConfirmParams.builder()
                    .setPaymentMethodData(PaymentIntentConfirmParams.PaymentMethodData.builder().setType(BLIK).build())
                    .setPaymentMethodOptions(PaymentIntentConfirmParams.PaymentMethodOptions.builder()
                            .setBlik(PaymentIntentConfirmParams.PaymentMethodOptions.Blik.builder().setCode(paymentConfirmRequest.getBlikCode()).build())
                        .build())
                    .build();

            if (!paymentConfirmRequest.getBlikCode().equals("123456")) {
                return "blik_code_error";
            }
            paymentById.confirm(params);

            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Thread was interrupted during sleep", e);
            }

            return pollPaymentStatus(paymentConfirmRequest.getPaymentId());
        } catch (StripeException e) {
            throw new RuntimeException(e);
        }
    }

    private String pollPaymentStatus(String paymentId) {
        try {
            PaymentIntent paymentIntent;
            String status;

            do {
                paymentIntent = findPaymentById(paymentId);
                status = paymentIntent.getStatus();

                int membershipTypeMonths = Integer.parseInt(paymentIntent.getMetadata().get("membership_type_months"));
                String email = paymentIntent.getMetadata().get("user_email");

                if (status.equals("succeeded")) {
                    membershipService.updateMembership(email, membershipTypeMonths);
                    return "success";
                } else if (status.equals("in_process")){
                    return "in_process";
                } else if (status.equals("payment_failed")) {
                    return "failed";
                }

                RETRY_COUNT++;
                if (RETRY_COUNT >= MAX_RETRY_COUNT) {
                    return "undefined_error";
                }

                Thread.sleep(DELAY_MILLIS);
            } while (!"succeeded".equals(status) || !"payment_failed".equals(status));

            return "undefined_error";
        } catch (StripeException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
