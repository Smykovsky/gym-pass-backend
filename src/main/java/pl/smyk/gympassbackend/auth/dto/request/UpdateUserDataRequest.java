package pl.smyk.gympassbackend.auth.dto.request;

import lombok.Data;
import pl.smyk.gympassbackend.auth.model.Gender;

@Data
public class UpdateUserDataRequest {
    private String firstName;
    private String lastName;
    private Gender gender;
    private String userEmail;
}
