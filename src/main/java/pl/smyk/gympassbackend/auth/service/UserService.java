package pl.smyk.gympassbackend.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.smyk.gympassbackend.auth.exception.UserNotFoundException;
import pl.smyk.gympassbackend.auth.model.User;
import pl.smyk.gympassbackend.auth.repository.UserRepository;

import java.io.IOException;
import java.util.Base64;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final AuthService authService;

    public void uploadProfileImage(String email, MultipartFile file) throws IOException {
        User user = authService.findByEmail(email).orElseThrow(() -> new UserNotFoundException(email));

        String base64Image = encodeImageToBase64(file);
        user.setProfileImage(base64Image);
        userRepository.save(user);
    }

    private String encodeImageToBase64(MultipartFile file) throws IOException {
        String contentType = file.getContentType();
        String base64 = Base64.getEncoder().encodeToString(file.getBytes());

        return "data:" + contentType + ";base64," + base64;
    }
}