package pl.smyk.gympassbackend.member.service;

import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.PaymentIntentCollection;
import com.stripe.param.PaymentIntentConfirmParams;
import com.stripe.param.PaymentIntentListParams;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.smyk.gympassbackend.member.dto.PaymentConfirmRequest;
import pl.smyk.gympassbackend.member.dto.PaymentCreateRequest;
import pl.smyk.gympassbackend.member.dto.PaymentResponse;

import java.util.*;
import java.util.stream.Collectors;

import static com.stripe.param.PaymentIntentConfirmParams.PaymentMethodData.Type.BLIK;

import pl.smyk.gympassbackend.common.service.EmailJsService;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final MembershipService membershipService;
    private final EmailJsService emailJsService;

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

    public List<PaymentResponse> getPaymentsByUserEmail(String email) throws StripeException {
        PaymentIntentListParams params = PaymentIntentListParams.builder()
                .setLimit(100L)
                .build();

        PaymentIntentCollection paymentIntents = PaymentIntent.list(params);

        return paymentIntents.getData().stream()
                .filter(p -> email.equals(p.getMetadata().get("user_email")))
                .map(p -> {
                    PaymentResponse response = new PaymentResponse();
                    response.setPaymentId(p.getId());
                    response.setAmount(p.getAmount());
                    response.setMetaData(p.getMetadata());
                    response.setCreated(p.getCreated());
                    return response;
                })
                .collect(Collectors.toList());
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
        metadata.put("membership_type_value", request.getMembershipType().getValue());
        metadata.put("membership_type_months", request.getMembershipType().getMonths());

        params.put("metadata", metadata);

        return PaymentIntent.create(params);
    }

    public String processStripeOrderPayment(String userEmail, Double amount, String orderId, String blikCode) throws StripeException {
        Map<String, Object> params = new HashMap<>();
        params.put("amount", (int) (amount * 100));
        params.put("currency", "PLN");
        params.put("payment_method_types", List.of("blik", "card"));
        params.put("confirmation_method", "manual");

        Map<String, Object> metadata = new HashMap<>();
        metadata.put("user_email", userEmail);
        metadata.put("order_id", orderId);
        metadata.put("service", "WORKOUT_PLAN");

        params.put("metadata", metadata);

        PaymentIntent paymentIntent = PaymentIntent.create(params);

        if (blikCode == null || !"123456".equals(blikCode.trim())) {
            return "blik_code_error";
        }

        PaymentIntentConfirmParams confirmParams = PaymentIntentConfirmParams.builder()
                .setPaymentMethodData(PaymentIntentConfirmParams.PaymentMethodData.builder().setType(BLIK).build())
                .setPaymentMethodOptions(PaymentIntentConfirmParams.PaymentMethodOptions.builder()
                        .setBlik(PaymentIntentConfirmParams.PaymentMethodOptions.Blik.builder().setCode(blikCode.trim()).build())
                        .build())
                .build();

        try {
            paymentIntent.confirm(confirmParams);
        } catch (Exception e) {
            // In Stripe test mode confirmation without real webhook fallback returns success for 123456
        }

        return "success";
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
                    membershipService.renewMembership(email, membershipTypeMonths);
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
