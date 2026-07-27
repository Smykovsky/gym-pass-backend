package pl.smyk.gympassbackend.auth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.smyk.gympassbackend.auth.dto.request.CreateTrainerRequest;
import pl.smyk.gympassbackend.auth.dto.response.AdminUserResponseDto;
import pl.smyk.gympassbackend.auth.exception.UserNotFoundException;
import pl.smyk.gympassbackend.auth.model.Achievement;
import pl.smyk.gympassbackend.auth.model.Role;
import pl.smyk.gympassbackend.auth.model.Trainer;
import pl.smyk.gympassbackend.auth.model.User;
import pl.smyk.gympassbackend.auth.repository.UserRepository;
import pl.smyk.gympassbackend.member.model.Membership;
import pl.smyk.gympassbackend.member.repository.MembershipRepository;
import pl.smyk.gympassbackend.member.util.CodeGenerator;
import pl.smyk.gympassbackend.training.model.TrainingType;
import pl.smyk.gympassbackend.common.service.EmailJsService;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MembershipRepository membershipRepository;
    private final CodeGenerator codeGenerator;
    private final EmailJsService emailJsService;

    public List<AdminUserResponseDto> getAllUsers(String search, Role roleFilter, Boolean lockedFilter) {
        List<User> users = userRepository.findAll();

        return users.stream()
                .filter(user -> {
                    if (search != null && !search.trim().isEmpty()) {
                        String query = search.toLowerCase().trim();
                        boolean matchEmail = user.getEmail() != null && user.getEmail().toLowerCase().contains(query);
                        boolean matchFirst = user.getFirstName() != null && user.getFirstName().toLowerCase().contains(query);
                        boolean matchLast = user.getLastName() != null && user.getLastName().toLowerCase().contains(query);
                        if (!matchEmail && !matchFirst && !matchLast) {
                            return false;
                        }
                    }
                    if (roleFilter != null) {
                        if (user.getRoles() == null || !user.getRoles().contains(roleFilter)) {
                            return false;
                        }
                    }
                    if (lockedFilter != null) {
                        boolean isLocked = !user.isAccountNonLocked();
                        if (isLocked != lockedFilter) {
                            return false;
                        }
                    }
                    return true;
                })
                .map(this::mapToAdminUserDto)
                .collect(Collectors.toList());
    }

    public AdminUserResponseDto getUserById(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Nie znaleziono użytkownika o podanym ID: " + userId));
        return mapToAdminUserDto(user);
    }

    public AdminUserResponseDto createTrainer(CreateTrainerRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Użytkownik o podanym adresie e-mail już istnieje!");
        }

        Trainer trainer = Trainer.builder()
                .email(request.getEmail())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .gender(request.getGender())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(List.of(Role.TRAINER, Role.USER))
                .certifications(request.getCertifications() != null ? request.getCertifications() : List.of())
                .trainingTypes(request.getTrainingTypes() != null ? request.getTrainingTypes() : List.of())
                .progress(Arrays.stream(Achievement.values())
                        .collect(Collectors.toMap(a -> a, a -> 0)))
                .build();

        Trainer saved = userRepository.save(trainer);

        Membership membership = new Membership();
        membership.generateCodes(codeGenerator, saved.getId());
        membershipRepository.save(membership);

        saved.setMembership(membership);
        User finalUser = userRepository.save(saved);

        emailJsService.sendWelcomeAccountCreatedEmail(finalUser.getEmail(), finalUser.getFirstName());

        log.info("Ręcznie utworzono konto trenera: {} (ID: {})", finalUser.getEmail(), finalUser.getId());
        return mapToAdminUserDto(finalUser);
    }

    public AdminUserResponseDto lockUser(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Nie znaleziono użytkownika o podanym ID: " + userId));
        user.setAccountNonLocked(false);
        User saved = userRepository.save(user);
        log.info("Konto użytkownika {} (ID: {}) zostało zablokowane.", saved.getEmail(), saved.getId());
        return mapToAdminUserDto(saved);
    }

    public AdminUserResponseDto unlockUser(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Nie znaleziono użytkownika o podanym ID: " + userId));
        user.setAccountNonLocked(true);
        User saved = userRepository.save(user);
        log.info("Konto użytkownika {} (ID: {}) zostało odblokowane.", saved.getEmail(), saved.getId());
        return mapToAdminUserDto(saved);
    }

    public AdminUserResponseDto updateUserRoles(String userId, List<Role> roles) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Nie znaleziono użytkownika o podanym ID: " + userId));
        if (roles == null || roles.isEmpty()) {
            throw new IllegalArgumentException("Lista ról nie może być pusta!");
        }
        user.setRoles(roles);
        User saved = userRepository.save(user);
        log.info("Zaktualizowano role użytkownika {} (ID: {}): {}", saved.getEmail(), saved.getId(), roles);
        return mapToAdminUserDto(saved);
    }

    private AdminUserResponseDto mapToAdminUserDto(User user) {
        AdminUserResponseDto.AdminMembershipInfo membershipInfo = null;

        Membership membership = user.getMembership();
        if (membership != null) {
            membershipInfo = AdminUserResponseDto.AdminMembershipInfo.builder()
                    .id(membership.getId())
                    .active(membership.isActive())
                    .startDate(membership.getStartDate())
                    .endDate(membership.getEndDate())
                    .membershipType(membership.getMembershipType())
                    .build();
        }

        List<String> certifications = null;
        List<TrainingType> trainingTypes = null;
        if (user instanceof Trainer trainer) {
            certifications = trainer.getCertifications();
            trainingTypes = trainer.getTrainingTypes();
        }

        return AdminUserResponseDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .gender(user.getGender())
                .roles(user.getRoles())
                .accountNonLocked(user.isAccountNonLocked())
                .profileImage(user.getProfileImage())
                .membership(membershipInfo)
                .certifications(certifications)
                .trainingTypes(trainingTypes)
                .build();
    }
}
