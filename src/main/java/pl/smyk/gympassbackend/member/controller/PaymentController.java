package pl.smyk.gympassbackend.member.controller;

import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.smyk.gympassbackend.member.dto.PaymentConfirmRequest;
import pl.smyk.gympassbackend.member.dto.PaymentCreateRequest;
import pl.smyk.gympassbackend.member.service.PaymentService;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping("/create")
    public ResponseEntity<?> createPayment(Authentication authentication, @RequestBody PaymentCreateRequest request) throws StripeException {
        request.setEmail(authentication.getName());
        PaymentIntent payment = paymentService.createPayment(request);
        return ResponseEntity.ok(payment.getId() + payment.getStatus());
    }

    @PostMapping("/confirm")
    public ResponseEntity<?> confirmPayment(Authentication authentication, @RequestBody PaymentConfirmRequest request) throws StripeException {
        String response = paymentService.confirmPayment(request);
        return ResponseEntity.ok(response);
    }
}
