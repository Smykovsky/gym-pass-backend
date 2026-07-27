package pl.smyk.gympassbackend.contest.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import pl.smyk.gympassbackend.member.model.MembershipType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document("contests")
public class Contest {

    @Id
    private String id;

    private String title;
    private String description;

    private MembershipType membershipType;
    private int durationMonths;

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    @Builder.Default
    private ContestStatus status = ContestStatus.ACTIVE;

    @Builder.Default
    private List<String> participantUserIds = new ArrayList<>();

    private String winnerUserId;
    private String winnerUserName;
    private String winnerUserEmail;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
