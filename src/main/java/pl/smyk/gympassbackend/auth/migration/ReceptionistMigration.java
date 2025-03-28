package pl.smyk.gympassbackend.auth.migration;

import io.mongock.api.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.smyk.gympassbackend.auth.model.Receptionist;
import pl.smyk.gympassbackend.auth.model.Role;
import pl.smyk.gympassbackend.auth.model.User;
import pl.smyk.gympassbackend.auth.repository.ReceptionistRepository;
import pl.smyk.gympassbackend.auth.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@ChangeUnit(order = "003", id = "receptionist-seeder", author = "dev")
@RequiredArgsConstructor
public class ReceptionistMigration {
    private final MongoTemplate mongoTemplate;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final ReceptionistRepository receptionistRepository;

    @BeforeExecution
    public void beforeSeedDatabase() {
        System.out.println("Before seeding trainers and receptionist...");
    }

    @RollbackBeforeExecution
    public void rollbackBeforeExecution() {
        System.out.println("Rollback before seeding trainers and receptionist...");
    }

    @Execution
    public void seedTrainersAndReceptionist() {
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

    @RollbackExecution
    public void rollbackSeedTrainersAndReceptionist() {
        System.out.println("Rollback trainers and receptionist...");
        receptionistRepository.findByEmail("r@example.com").ifPresent(receptionistRepository::delete);
    }
}
