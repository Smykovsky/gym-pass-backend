package pl.smyk.gympassbackend.auth.dto;

import lombok.Data;
import pl.smyk.gympassbackend.member.dto.MembershipDto;

@Data
public class UserDto {
    private String id;
    private String email;
    private String firstName;
    private String lastName;
    private MembershipDto membership;
}
