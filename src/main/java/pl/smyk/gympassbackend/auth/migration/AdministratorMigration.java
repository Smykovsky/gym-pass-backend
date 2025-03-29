package pl.smyk.gympassbackend.auth.migration;

import io.mongock.api.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.smyk.gympassbackend.auth.model.Role;
import pl.smyk.gympassbackend.auth.model.User;
import pl.smyk.gympassbackend.auth.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@ChangeUnit(order = "002", id = "administrator-seeder", author = "dev")
@RequiredArgsConstructor
public class AdministratorMigration {
    private final MongoTemplate mongoTemplate;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

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
        Optional<User> byEmail = userRepository.findByEmail("admin@example.com");
        if (byEmail.isPresent()) {
            return;
        }

        User admin = User.builder()
                .email("admin@example.com")
                .firstName("Admin")
                .lastName("Admin")
                .password(passwordEncoder.encode("12345678"))
                .roles(List.of(Role.ADMIN, Role.USER))
                .build();

        userRepository.save(admin);
    }

    @RollbackExecution
    public void rollbackSeedTrainersAndReceptionist() {
        System.out.println("Rollback trainers and receptionist...");
        userRepository.findByEmail("admin@example.com").ifPresent(userRepository::delete);
    }
}
