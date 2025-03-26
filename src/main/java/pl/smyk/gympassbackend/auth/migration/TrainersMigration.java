package pl.smyk.gympassbackend.auth.migration;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.data.mongodb.core.MongoTemplate;
import pl.smyk.gympassbackend.auth.model.Receptionist;
import pl.smyk.gympassbackend.auth.model.Role;
import pl.smyk.gympassbackend.auth.model.Trainer;
import pl.smyk.gympassbackend.auth.model.User;
import pl.smyk.gympassbackend.auth.repository.TrainerRepository;
import pl.smyk.gympassbackend.auth.repository.UserRepository;
import pl.smyk.gympassbackend.member.model.Membership;
import pl.smyk.gympassbackend.member.repository.MembershipRepository;
import pl.smyk.gympassbackend.member.util.CodeGenerator;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
public class TrainersMigration implements CommandLineRunner {

  private final MongoTemplate mongoTemplate;
  private final PasswordEncoder passwordEncoder;
  private final UserRepository userRepository;
  private final TrainerRepository trainerRepository;
  private final MembershipRepository membershipRepository;
  private final CodeGenerator codeGenerator;

  public TrainersMigration(
    MongoTemplate mongoTemplate,
    PasswordEncoder passwordEncoder,
    UserRepository userRepository,
    TrainerRepository trainerRepository,
    MembershipRepository membershipRepository,
    CodeGenerator codeGenerator) {

    this.mongoTemplate = mongoTemplate;
    this.passwordEncoder = passwordEncoder;
    this.userRepository = userRepository;
    this.trainerRepository = trainerRepository;
    this.membershipRepository = membershipRepository;
    this.codeGenerator = codeGenerator;
  }

  @Override
  public void run(String... args) throws Exception {
    // Sprawdzenie, czy trener już istnieje
    Optional<Trainer> existingTrainer = trainerRepository.findByEmail("t@example.com");
    if (existingTrainer.isEmpty()) {
      Trainer trainer = Trainer.builder()
        .email("t@example.com")
        .firstName("Trener")
        .lastName("Trener")
        .password(passwordEncoder.encode("12345678"))
        .roles(List.of(Role.TRAINER, Role.USER))
        .certifications(List.of("Crossfit", "Dietitian"))
        .build();

      Trainer savedTrainer = userRepository.save(trainer);

      Membership membership = new Membership();
      membership.generateCodes(codeGenerator, savedTrainer.getId());
      membershipRepository.save(membership);

      savedTrainer.setMembership(membership);
      userRepository.save(savedTrainer);

      Trainer trainerFromDb = trainerRepository.findByEmail("t@example.com").orElseThrow();
      trainerFromDb.getMembership().setStartDate(LocalDate.now());
      trainerFromDb.getMembership().setEndDate(LocalDate.now().plusMonths(2));
      trainerFromDb.getMembership().setActive(true);

      membershipRepository.save(trainerFromDb.getMembership());
      trainerRepository.save(trainerFromDb);
    }

    // Sprawdzenie, czy recepcjonistka już istnieje
    Optional<User> existingReceptionist = userRepository.findByEmail("r@example.com");
    if (existingReceptionist.isEmpty()) {
      Receptionist receptionist = Receptionist.builder()
        .email("r@example.com")
        .firstName("Recepcjonistka")
        .lastName("Recepcjonistka")
        .password(passwordEncoder.encode("12345678"))
        .roles(List.of(Role.RECEPTIONIST, Role.USER))
        .build();

      userRepository.save(receptionist);
    }
  }
}
