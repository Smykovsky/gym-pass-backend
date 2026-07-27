package pl.smyk.gympassbackend.auth.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.smyk.gympassbackend.auth.model.Role;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangeUserRolesRequest {
    private List<Role> roles;
}
