package pl.smyk.gympassbackend.contest.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

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

    /** Code of the {@link pl.smyk.gympassbackend.member.model.MembershipPlan} awarded as the prize. */
    private String membershipType;
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
