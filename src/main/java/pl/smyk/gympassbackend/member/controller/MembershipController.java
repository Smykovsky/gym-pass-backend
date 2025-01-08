package pl.smyk.gympassbackend.member.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.smyk.gympassbackend.member.model.Membership;
import pl.smyk.gympassbackend.member.service.MembershipService;

@RestController
@RequestMapping("/api/membership")
@RequiredArgsConstructor
public class MembershipController {
    private final MembershipService membershipService;

    @GetMapping()
    public ResponseEntity<?> getMembershipByUserEmail(Authentication authentication) {
        String email = authentication.getName();
        if (email.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Membership membershipByEmail = membershipService.getMembershipByEmail(email);
        return ResponseEntity.ok(membershipByEmail);
    }
}
