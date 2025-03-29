package pl.smyk.gympassbackend.auth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.smyk.gympassbackend.member.dto.ValidateMemberResponse;
import pl.smyk.gympassbackend.member.service.MembershipService;

@RestController
@RequestMapping("/api/receptionist")
@PreAuthorize("hasAuthority('ROLE_RECEPTIONIST')")
@RequiredArgsConstructor
public class ReceptionistController {
  private final MembershipService membershipService;


  @PostMapping("/validate/{qrCodeData}")
  @PreAuthorize("hasAuthority('ROLE_RECEPTIONIST')")
  public ResponseEntity<?> validateMembership(Authentication authentication, @PathVariable String qrCodeData) throws Exception {
    if (!authentication.isAuthenticated()) {
      return ResponseEntity.status(401).body("Unauthorized");
    }

    ValidateMemberResponse validateMemberResponse = membershipService.scanUserMembershipPass(qrCodeData);

    return ResponseEntity.ok(validateMemberResponse);
  }
}
