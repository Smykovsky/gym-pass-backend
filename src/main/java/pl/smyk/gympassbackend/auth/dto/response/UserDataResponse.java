package pl.smyk.gympassbackend.auth.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import pl.smyk.gympassbackend.auth.model.Achievement;
import pl.smyk.gympassbackend.auth.model.Gender;
import pl.smyk.gympassbackend.member.model.MembershipType;

import java.time.LocalDate;
import java.util.Map;

@Builder
@Getter
@Setter
public class UserDataResponse {
    private String email;
    private String firstName;
    private String lastName;
    private Gender gender;
    private MembershipData membershipData;
    private Map<Achievement, Integer> progress;

    @Builder
    @Getter
    @Setter
    public static class MembershipData {
        private boolean active;
        private LocalDate startDate;
        private LocalDate endDate;
        private String qrCode;
        private MembershipType membershipType;
    }
}
