package pl.smyk.gympassbackend;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.smyk.gympassbackend.auth.dto.UserDto;
import pl.smyk.gympassbackend.auth.mapper.UserMapper;
import pl.smyk.gympassbackend.auth.model.Receptionist;
import pl.smyk.gympassbackend.auth.model.Role;
import pl.smyk.gympassbackend.auth.model.Trainer;
import pl.smyk.gympassbackend.auth.model.User;
import pl.smyk.gympassbackend.auth.repository.TrainerRepository;
import pl.smyk.gympassbackend.auth.repository.UserRepository;
import pl.smyk.gympassbackend.auth.service.AuthService;
import pl.smyk.gympassbackend.member.dto.MembershipDto;
import pl.smyk.gympassbackend.member.mapper.MembershipMapper;
import pl.smyk.gympassbackend.member.model.Membership;
import pl.smyk.gympassbackend.member.repository.MembershipRepository;
import pl.smyk.gympassbackend.member.util.AESUtils;
import pl.smyk.gympassbackend.member.util.CodeGenerator;
import pl.smyk.gympassbackend.training.model.ScheduledTraining;
import pl.smyk.gympassbackend.training.model.TrainingStatus;
import pl.smyk.gympassbackend.training.repository.ScheduledTrainingRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@SpringBootApplication
@RequiredArgsConstructor
public class GymPassBackendApplication implements CommandLineRunner {
    private final UserRepository userRepository;
    private final TrainerRepository trainerRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthService authService;
    private final CodeGenerator codeGenerator;
    private final ScheduledTrainingRepository scheduledTrainingRepository;
    private final MembershipRepository membershipRepository;
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    public static void main(String[] args) {
        SpringApplication.run(GymPassBackendApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
//        Trainer trainer = Trainer.builder()
//                .email("t@example.com")
//                .firstName("Trener")
//                .lastName("Trener")
//                .password(passwordEncoder.encode("12345678"))
//                .roles(List.of(Role.TRAINER, Role.USER))
//                .certifications(List.of("Crossfit", "Dietitian"))
//                .build();
//
//        Trainer saved = userRepository.save(trainer);
//
//        Membership membership = new Membership();
//        membership.generateCodes(codeGenerator, saved.getId());
//        membershipRepository.save(membership);
//
//        saved.setMembership(membership);
//
//        userRepository.save(saved);
//
//        Optional<Trainer> byEmail = trainerRepository.findByEmail("t@example.com");
//        Trainer trainer1 = byEmail.get();
//        trainer1.getMembership().setStartDate(LocalDate.now());
//        trainer1.getMembership().setEndDate(LocalDate.now().plusMonths(2));
//        trainer1.getMembership().setActive(true);
//
//        membershipRepository.save(trainer.getMembership());
//
//        trainerRepository.save(trainer);
//
////
//        Receptionist receptionist = Receptionist.builder()
//                .email("r@example.com")
//                .firstName("recepcjonistka")
//                .lastName("recepcjonistka")
//                .password(passwordEncoder.encode("12345678"))
//                .roles(List.of(Role.RECEPTIONIST, Role.USER))
//                .build();
//
//        userRepository.save(receptionist);
//
//        Trainer byId = trainerRepository.findById("674f04eb1047ef0c6d3fdff3").orElse(null);
//        System.out.println(byId.getUsername() + byId.getRoles());
//
//        String test = "uM5fZRDSAoDNp51mCxc+GGUFkKIubYwakur24ew+GGA=";
//
//        String decrypt = AESUtils.decrypt(test);
//        System.out.println(decrypt);

//        String encrypted = AESUtils.decrypt("1nyCYKAcV8GSzMhVVhqEvDddeTWwxV9Xk0QS6rdsrr8=");
//        System.out.println(encrypted);
//
//        Optional<Trainer> trainerByEmail = trainerRepository.findByEmail("trainer@example.com");
//
//        Trainer trainer = trainerByEmail.get();
//        User user = authService.findByEmail("smyku1232@wp.pl").get();
//
//        ScheduledTraining newTraining = ScheduledTraining.builder().trainingDateTime(LocalDateTime.now()).trainer(trainer).user(user).trainingStatus(TrainingStatus.WAITING).build();
//        scheduledTrainingRepository.save(newTraining);
    }
}
