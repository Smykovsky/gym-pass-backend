package pl.smyk.gympassbackend.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.smyk.gympassbackend.auth.config.jwt.JwtUtil;
import pl.smyk.gympassbackend.auth.dto.request.LoginRequest;
import pl.smyk.gympassbackend.auth.dto.request.RegisterRequest;
import pl.smyk.gympassbackend.auth.dto.response.AuthResponse;
import pl.smyk.gympassbackend.auth.model.Role;
import pl.smyk.gympassbackend.auth.model.User;
import pl.smyk.gympassbackend.auth.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public String register(RegisterRequest registerRequest) {
        var user = User.builder()
                .email(registerRequest.getEmail())
                .username(registerRequest.getUsername())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .barCode(null)
                .qrCode(null)
                .roles(List.of(Role.USER))
                .build();

        var savedUser = userRepository.save(user);

        return savedUser.getEmail();
    }

    public AuthResponse login(LoginRequest loginRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(), loginRequest.getPassword()
                )
        );

        var user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow();
        var jwtToken = jwtUtil.generateToken(user);
        var refreshToken = jwtUtil.generateRefreshToken(user);
        return new AuthResponse(jwtToken, refreshToken);
    }
}
