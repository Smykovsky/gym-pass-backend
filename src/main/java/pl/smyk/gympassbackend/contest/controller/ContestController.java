package pl.smyk.gympassbackend.contest.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pl.smyk.gympassbackend.contest.dto.ContestDto;
import pl.smyk.gympassbackend.contest.dto.CreateContestRequest;
import pl.smyk.gympassbackend.contest.service.ContestService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ContestController {

    private final ContestService contestService;

    // --- USER / MEMBER ENDPOINTS ---
    @GetMapping("/api/user/contests")
    public ResponseEntity<List<ContestDto>> getUserContests(Authentication authentication) {
        return ResponseEntity.ok(contestService.getAllContestsForUser(authentication.getName()));
    }

    @PostMapping("/api/user/contests/{id}/join")
    public ResponseEntity<ContestDto> joinContest(Authentication authentication, @PathVariable String id) {
        return ResponseEntity.ok(contestService.joinContest(authentication.getName(), id));
    }

    @DeleteMapping("/api/user/contests/{id}/leave")
    public ResponseEntity<ContestDto> leaveContest(Authentication authentication, @PathVariable String id) {
        return ResponseEntity.ok(contestService.leaveContest(authentication.getName(), id));
    }

    // --- ADMIN ENDPOINTS ---
    @PostMapping("/api/admin/contests")
    public ResponseEntity<ContestDto> createContest(Authentication authentication, @RequestBody CreateContestRequest request) {
        return ResponseEntity.ok(contestService.createContest(request));
    }

    @GetMapping("/api/admin/contests")
    public ResponseEntity<List<ContestDto>> getAdminContests(Authentication authentication) {
        return ResponseEntity.ok(contestService.getAllContestsForAdmin());
    }

    @PostMapping("/api/admin/contests/{id}/draw")
    public ResponseEntity<ContestDto> manualDrawWinner(Authentication authentication, @PathVariable String id) {
        return ResponseEntity.ok(contestService.manualDrawWinner(id));
    }
}
