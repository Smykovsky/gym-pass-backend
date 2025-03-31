package pl.smyk.gympassbackend.auth.dto;

import lombok.Data;
import pl.smyk.gympassbackend.auth.model.Achievement;
import pl.smyk.gympassbackend.member.dto.MembershipDto;

import java.util.HashMap;
import java.util.Map;

@Data
public class UserDto {
    private String id;
    private String email;
    private String firstName;
    private String lastName;
    private MembershipDto membership;
    private Map<Achievement, Integer> progress = new HashMap<>();
}
