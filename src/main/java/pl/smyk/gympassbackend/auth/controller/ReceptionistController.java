package pl.smyk.gympassbackend.auth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pl.smyk.gympassbackend.member.dto.AddUserEntry;
import pl.smyk.gympassbackend.member.dto.ValidateMemberResponse;
import pl.smyk.gympassbackend.member.service.EntryService;
import pl.smyk.gympassbackend.member.service.MembershipService;

@RestController
@RequestMapping("/api/receptionist")
@PreAuthorize("hasAuthority('ROLE_RECEPTIONIST')")
@RequiredArgsConstructor
public class ReceptionistController {
  private final MembershipService membershipService;
  private final EntryService entryService;

    @PostMapping("/validate")
    public ResponseEntity<ValidateMemberResponse> validateMembership(
            Authentication authentication,
            @RequestBody String qrCodeData
    ) throws Exception {
        if (!authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }

        qrCodeData = qrCodeData.replace("\"", "");

        ValidateMemberResponse response =
                membershipService.scanUserMembershipPass(qrCodeData);

        return ResponseEntity.ok(response);
    }
  @PostMapping("/entry")
  public ResponseEntity<?> addEntry(Authentication authentication, @RequestBody AddUserEntry entryDto) {
    if (!authentication.isAuthenticated()) {
      return ResponseEntity.status(401).build();
    }

    entryService.addUserEntry(entryDto.getUserEmail(), entryDto.getMembershipCardId());
    return ResponseEntity.ok("Dodano wejście dla użytkownika: " + entryDto.getUserEmail());
  }
}
