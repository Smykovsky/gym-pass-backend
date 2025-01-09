package pl.smyk.gympassbackend.auth.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.TypeAlias;
import pl.smyk.gympassbackend.auth.model.User;

@TypeAlias("receptionist")
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class Receptionist extends User {
    private String receptionistName;
}
