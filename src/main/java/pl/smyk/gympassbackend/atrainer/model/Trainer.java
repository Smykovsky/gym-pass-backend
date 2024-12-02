package pl.smyk.gympassbackend.atrainer.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.TypeAlias;
import pl.smyk.gympassbackend.auth.model.Role;
import pl.smyk.gympassbackend.auth.model.User;
import pl.smyk.gympassbackend.member.model.Membership;

import java.util.List;

@TypeAlias("trainer")
@Getter
@Setter
@SuperBuilder
public class Trainer extends User {
    private List<String> certifications;
    private String specialization;
}
