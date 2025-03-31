package pl.smyk.gympassbackend.member.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pl.smyk.gympassbackend.member.dto.MembershipDto;
import pl.smyk.gympassbackend.member.dto.UserEntriesResponse;
import pl.smyk.gympassbackend.member.dto.ValidateMemberResponse;
import pl.smyk.gympassbackend.member.service.EntryService;
import pl.smyk.gympassbackend.member.service.MembershipService;

import java.util.HashMap;

@RestController
@RequestMapping("/api/membership")
@RequiredArgsConstructor
public class MembershipController {
    private final MembershipService membershipService;
    private final EntryService entryService;

    @GetMapping()
    public ResponseEntity<?> getMembershipByUserEmail(Authentication authentication) {
        String email = authentication.getName();
        if (email.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        MembershipDto membershipByEmail = membershipService.getMembershipByEmail(email);
        return ResponseEntity.ok(membershipByEmail);
    }

    @GetMapping("/entries")
    public ResponseEntity<?> getMembershipEntries(Authentication authentication) {
        String email = authentication.getName();
        if (email.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        UserEntriesResponse allUserEntries = entryService.findAllUserEntries(email);
        return ResponseEntity.ok(allUserEntries);
    }
}
