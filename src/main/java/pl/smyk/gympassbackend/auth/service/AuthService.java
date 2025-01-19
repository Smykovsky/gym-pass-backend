package pl.smyk.gympassbackend.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.smyk.gympassbackend.auth.config.jwt.JwtUtil;
import pl.smyk.gympassbackend.auth.dto.request.LoginRequest;
import pl.smyk.gympassbackend.auth.dto.request.RegisterRequest;
import pl.smyk.gympassbackend.auth.dto.response.AuthResponse;
import pl.smyk.gympassbackend.auth.dto.response.UserDataResponse;
import pl.smyk.gympassbackend.auth.model.Role;
import pl.smyk.gympassbackend.auth.model.User;
import pl.smyk.gympassbackend.auth.repository.UserRepository;
import pl.smyk.gympassbackend.member.model.Membership;
import pl.smyk.gympassbackend.member.repository.MembershipRepository;
import pl.smyk.gympassbackend.member.util.CodeGenerator;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final MembershipRepository membershipRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final CodeGenerator codeGenerator;

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> findById(String id) {
        return userRepository.findById(id);
    }

    public void save(User user) {
        userRepository.save(user);
    }

    @Transactional
    public AuthResponse register(RegisterRequest registerRequest) {
        var user = User.builder()
                .email(registerRequest.getEmail())
                .firstName(registerRequest.getFirstName())
                .lastName(registerRequest.getLastName())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .roles(List.of(Role.USER))
                .build();

        var savedUser = userRepository.save(user);

        Membership membership = new Membership();
        membership.generateCodes(codeGenerator, user.getId());
        membershipRepository.save(membership);

        savedUser.setMembership(membership);
        userRepository.save(savedUser);

        return AuthResponse.builder().message("Rejestracja przebiegła pomyślnie!").build();
    }

    public AuthResponse login(LoginRequest loginRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(), loginRequest.getPassword()
                    )
            );

            var user = userRepository.findByEmail(loginRequest.getEmail())
                    .orElseThrow(() -> new RuntimeException("Nie znaleziono użytkownika o podanym adresie email!"));

            var jwtToken = jwtUtil.generateToken(user);
            var refreshToken = jwtUtil.generateRefreshToken(user);

            return AuthResponse.builder().accessToken(jwtToken).refreshToken(refreshToken).message("Pomyślnie zalogowano!").build();
        } catch (BadCredentialsException e) {
            throw new RuntimeException("Podane poświadczenia są nie prawdiłowe!", e);
        } catch (RuntimeException e) {
            throw new RuntimeException("Błąd podczas logowania: " + e.getMessage(), e);
        }
    }

    public UserDataResponse getUserData(String email) {
        if (email.isEmpty()) {
            return null;
        }

        Optional<User> byEmail = userRepository.findByEmail(email);
        if (!byEmail.isPresent()) {
            return null;
        }

        User user = byEmail.get();
        return UserDataResponse.builder()
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .membershipData(
                        UserDataResponse.MembershipData.builder()
                                .active(user.getMembership().isActive())
                                .startDate(user.getMembership().getStartDate())
                                .endDate(user.getMembership().getEndDate())
                                .qrCode(user.getMembership().getQrCode())
                                .membershipType(user.getMembership().getMembershipType())
                                .build()).build();
    }
}
