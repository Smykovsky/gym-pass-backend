package pl.smyk.gympassbackend.member.controller;

import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pl.smyk.gympassbackend.member.dto.PaymentConfirmRequest;
import pl.smyk.gympassbackend.member.dto.PaymentCreateRequest;
import pl.smyk.gympassbackend.member.service.PaymentService;
import pl.smyk.gympassbackend.member.dto.PaymentResponse;

import java.util.List;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping("/create")
    public ResponseEntity<?> createPayment(Authentication authentication, @RequestBody PaymentCreateRequest request) throws StripeException {
        request.setEmail(authentication.getName());
        PaymentIntent payment = paymentService.createPayment(request);
        return ResponseEntity.ok(payment.getId());
    }

    @PostMapping("/confirm")
    public ResponseEntity<?> confirmPayment(Authentication authentication, @RequestBody PaymentConfirmRequest request) throws StripeException {
        String response = paymentService.confirmPayment(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getPaymentById(Authentication authentication, @PathVariable String id) throws StripeException {
        PaymentIntent paymentById = paymentService.findPaymentById(id);
        PaymentResponse response = new PaymentResponse();
        response.setPaymentId(paymentById.getId());
        response.setAmount(paymentById.getAmount());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/history")
    public ResponseEntity<?> getUserPayments(Authentication authentication) throws StripeException {
        List<PaymentResponse> paymentsByUserEmail = paymentService.getPaymentsByUserEmail(authentication.getName());
        System.out.println(paymentsByUserEmail);
        return ResponseEntity.ok(paymentsByUserEmail);
    }
}
