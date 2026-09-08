package pl.smyk.gympassbackend.member.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.smyk.gympassbackend.member.dto.MembershipPlanRequest;
import pl.smyk.gympassbackend.member.dto.MembershipPlanResponse;
import pl.smyk.gympassbackend.member.service.MembershipPlanService;

import java.util.List;

@RestController
@RequestMapping("/api/admin/membership-plans")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
@RequiredArgsConstructor
public class AdminMembershipPlanController {

    private final MembershipPlanService membershipPlanService;

    @GetMapping
    public ResponseEntity<List<MembershipPlanResponse>> getAll() {
        List<MembershipPlanResponse> plans = membershipPlanService.getAllPlans().stream()
                .map(MembershipPlanResponse::from)
                .toList();
        return ResponseEntity.ok(plans);
    }

    @PostMapping
    public ResponseEntity<MembershipPlanResponse> create(@RequestBody MembershipPlanRequest request) {
        return ResponseEntity.ok(MembershipPlanResponse.from(membershipPlanService.create(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MembershipPlanResponse> update(@PathVariable String id,
                                                        @RequestBody MembershipPlanRequest request) {
        return ResponseEntity.ok(MembershipPlanResponse.from(membershipPlanService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        membershipPlanService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
