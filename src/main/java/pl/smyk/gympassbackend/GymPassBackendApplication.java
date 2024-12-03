package pl.smyk.gympassbackend;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import pl.smyk.gympassbackend.atrainer.model.Trainer;
import pl.smyk.gympassbackend.atrainer.repository.TrainerRepository;
import pl.smyk.gympassbackend.auth.model.Role;
import pl.smyk.gympassbackend.auth.repository.UserRepository;

import java.util.List;
import java.util.Optional;


@SpringBootApplication
@RequiredArgsConstructor
public class GymPassBackendApplication implements CommandLineRunner {
    private final UserRepository userRepository;
    private final TrainerRepository trainerRepository;
    public static void main(String[] args) {
        SpringApplication.run(GymPassBackendApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
//        Trainer trainer = Trainer.builder()
//                .email("trainer@example.com")
//                .username("trainer1")
//                .password("hashed_password")
//                .roles(List.of(Role.TRAINER, Role.USER))
//                .certifications(List.of("Crossfit", "Dietitian"))
//                .build();
//
//        userRepository.save(trainer);

        Trainer byId = trainerRepository.findById("674f04eb1047ef0c6d3fdff3").orElse(null);
        System.out.println(byId.getUsername() + byId.getRoles());
    }
}
