package pl.smyk.gympassbackend.member.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pl.smyk.gympassbackend.member.dto.MembershipDto;
import pl.smyk.gympassbackend.member.dto.ValidateMemberResponse;
import pl.smyk.gympassbackend.member.service.MembershipService;

import java.util.HashMap;

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

        MembershipDto membershipByEmail = membershipService.getMembershipByEmail(email);
        return ResponseEntity.ok(membershipByEmail);
    }
}
