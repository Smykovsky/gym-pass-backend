package pl.smyk.gympassbackend.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.smyk.gympassbackend.auth.model.Gender;
import pl.smyk.gympassbackend.auth.model.Role;
import pl.smyk.gympassbackend.member.model.MembershipType;
import pl.smyk.gympassbackend.training.model.TrainingType;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminUserResponseDto {
    private String id;
    private String email;
    private String firstName;
    private String lastName;
    private Gender gender;
    private List<Role> roles;
    private boolean accountNonLocked;
    private String profileImage;
    private AdminMembershipInfo membership;
    private List<String> certifications;
    private List<TrainingType> trainingTypes;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AdminMembershipInfo {
        private String id;
        private boolean active;
        private LocalDate startDate;
        private LocalDate endDate;
        private MembershipType membershipType;
    }
}
