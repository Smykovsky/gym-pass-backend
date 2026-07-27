package pl.smyk.gympassbackend.auth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.smyk.gympassbackend.auth.dto.request.ChangeUserRolesRequest;
import pl.smyk.gympassbackend.auth.dto.request.CreateTrainerRequest;
import pl.smyk.gympassbackend.auth.dto.response.AdminUserResponseDto;
import pl.smyk.gympassbackend.auth.model.Role;
import pl.smyk.gympassbackend.auth.service.AdminService;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/users")
    public ResponseEntity<List<AdminUserResponseDto>> getAllUsers(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String role,
            @RequestParam(required = false) Boolean locked
    ) {
        Role roleEnum = null;
        if (role != null && !role.trim().isEmpty()) {
            String roleClean = role.trim().toUpperCase();
            if (roleClean.startsWith("ROLE_")) {
                roleClean = roleClean.substring(5);
            }
            try {
                roleEnum = Role.valueOf(roleClean);
            } catch (IllegalArgumentException ignored) {
            }
        }
        List<AdminUserResponseDto> users = adminService.getAllUsers(search, roleEnum, locked);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<AdminUserResponseDto> getUserById(@PathVariable String userId) {
        AdminUserResponseDto user = adminService.getUserById(userId);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/trainers")
    public ResponseEntity<AdminUserResponseDto> createTrainer(@RequestBody CreateTrainerRequest request) {
        AdminUserResponseDto trainer = adminService.createTrainer(request);
        return ResponseEntity.ok(trainer);
    }

    @RequestMapping(value = "/users/{userId}/lock", method = {RequestMethod.PATCH, RequestMethod.POST, RequestMethod.PUT})
    public ResponseEntity<AdminUserResponseDto> lockUser(@PathVariable String userId) {
        AdminUserResponseDto updatedUser = adminService.lockUser(userId);
        return ResponseEntity.ok(updatedUser);
    }

    @RequestMapping(value = "/users/{userId}/unlock", method = {RequestMethod.PATCH, RequestMethod.POST, RequestMethod.PUT})
    public ResponseEntity<AdminUserResponseDto> unlockUser(@PathVariable String userId) {
        AdminUserResponseDto updatedUser = adminService.unlockUser(userId);
        return ResponseEntity.ok(updatedUser);
    }

    @RequestMapping(value = "/users/{userId}/roles", method = {RequestMethod.PATCH, RequestMethod.POST, RequestMethod.PUT})
    public ResponseEntity<AdminUserResponseDto> updateUserRoles(
            @PathVariable String userId,
            @RequestBody ChangeUserRolesRequest request
    ) {
        AdminUserResponseDto updatedUser = adminService.updateUserRoles(userId, request.getRoles());
        return ResponseEntity.ok(updatedUser);
    }
}
