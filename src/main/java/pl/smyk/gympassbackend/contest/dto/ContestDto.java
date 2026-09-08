package pl.smyk.gympassbackend.contest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.smyk.gympassbackend.contest.model.ContestStatus;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContestDto {
    private String id;
    private String title;
    private String description;
    private String membershipType;
    private int durationMonths;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private ContestStatus status;
    private int participantCount;
    @JsonProperty("isUserJoined")
    private boolean isUserJoined;
    private String winnerUserId;
    private String winnerUserName;
    private String winnerUserEmail;
    private LocalDateTime createdAt;
}
