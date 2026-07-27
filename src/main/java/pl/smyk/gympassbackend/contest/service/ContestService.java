package pl.smyk.gympassbackend.contest.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pl.smyk.gympassbackend.auth.exception.UserNotFoundException;
import pl.smyk.gympassbackend.auth.model.User;
import pl.smyk.gympassbackend.auth.repository.UserRepository;
import pl.smyk.gympassbackend.common.service.EmailJsService;
import pl.smyk.gympassbackend.contest.dto.ContestDto;
import pl.smyk.gympassbackend.contest.dto.CreateContestRequest;
import pl.smyk.gympassbackend.contest.model.Contest;
import pl.smyk.gympassbackend.contest.model.ContestStatus;
import pl.smyk.gympassbackend.contest.repository.ContestRepository;
import pl.smyk.gympassbackend.member.model.MembershipType;
import pl.smyk.gympassbackend.member.service.MembershipService;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContestService {

    private final ContestRepository contestRepository;
    private final UserRepository userRepository;
    private final MembershipService membershipService;
    private final EmailJsService emailJsService;
    private final SecureRandom random = new SecureRandom();

    public ContestDto createContest(CreateContestRequest request) {
        LocalDateTime now = LocalDateTime.now();
        int days = request.getDurationDays() != null && request.getDurationDays() > 0 ? request.getDurationDays() : 7;
        MembershipType type = request.getMembershipType() != null ? request.getMembershipType() : MembershipType.QUARTERLY;

        Contest contest = Contest.builder()
                .title(request.getTitle() != null ? request.getTitle().trim() : "Konkurs na Karnet!")
                .description(request.getDescription() != null ? request.getDescription().trim() : "")
                .membershipType(type)
                .durationMonths(type.getMonths())
                .startDate(now)
                .endDate(now.plusDays(days))
                .status(ContestStatus.ACTIVE)
                .participantUserIds(new ArrayList<>())
                .createdAt(now)
                .updatedAt(now)
                .build();

        Contest saved = contestRepository.save(contest);
        log.info("Utworzono nowy konkurs: '{}' (ID: {}), wygasa: {}", saved.getTitle(), saved.getId(), saved.getEndDate());
        return mapToDto(saved, null);
    }

    public ContestDto joinContest(String userEmail, String contestId) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("Nie znaleziono użytkownika!"));

        Contest contest = contestRepository.findById(contestId)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono konkursu o podanym ID: " + contestId));

        if (contest.getStatus() != ContestStatus.ACTIVE || LocalDateTime.now().isAfter(contest.getEndDate())) {
            throw new IllegalArgumentException("Ten konkurs jest już zakończony lub nieaktywny!");
        }

        if (contest.getParticipantUserIds().contains(user.getId())) {
            throw new IllegalArgumentException("Bierzesz już udział w tym konkursie!");
        }

        contest.getParticipantUserIds().add(user.getId());
        contest.setUpdatedAt(LocalDateTime.now());
        Contest saved = contestRepository.save(contest);

        log.info("Użytkownik {} (ID: {}) dołączył do konkursu {}", userEmail, user.getId(), contestId);
        return mapToDto(saved, user.getId());
    }

    @Scheduled(fixedRate = 60000) // Co 1 minutę
    public void checkAndSettleExpiredContests() {
        LocalDateTime now = LocalDateTime.now();
        List<Contest> expiredContests = contestRepository.findAllByStatusAndEndDateBefore(ContestStatus.ACTIVE, now);

        for (Contest contest : expiredContests) {
            log.info("Automatyczne rozliczanie wygasłego konkursu ID: {} ('{}')", contest.getId(), contest.getTitle());
            drawWinnerInternal(contest);
        }
    }

    public ContestDto leaveContest(String userEmail, String contestId) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("Nie znaleziono użytkownika!"));

        Contest contest = contestRepository.findById(contestId)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono konkursu o podanym ID: " + contestId));

        if (contest.getStatus() != ContestStatus.ACTIVE) {
            throw new IllegalArgumentException("Nie można opuścić zakończonego konkursu!");
        }

        if (!contest.getParticipantUserIds().contains(user.getId())) {
            throw new IllegalArgumentException("Nie jesteś uczestnikiem tego konkursu!");
        }

        contest.getParticipantUserIds().remove(user.getId());
        contest.setUpdatedAt(LocalDateTime.now());
        Contest saved = contestRepository.save(contest);

        log.info("Użytkownik {} (ID: {}) opuścił konkurs {}", userEmail, user.getId(), contestId);
        return mapToDto(saved, user.getId());
    }

    public ContestDto manualDrawWinner(String contestId) {
        Contest contest = contestRepository.findById(contestId)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono konkursu!"));

        if (contest.getStatus() != ContestStatus.ACTIVE) {
            throw new IllegalArgumentException("Konkurs nie jest aktywny!");
        }

        Contest settled = drawWinnerInternal(contest);
        return mapToDto(settled, null);
    }

    private Contest drawWinnerInternal(Contest contest) {
        List<String> participants = contest.getParticipantUserIds();

        if (participants == null || participants.isEmpty()) {
            log.info("Brak uczestników w konkursie ID: {}. Oznaczono jako zakończony bez zwycięzcy.", contest.getId());
            contest.setStatus(ContestStatus.FINISHED);
            contest.setUpdatedAt(LocalDateTime.now());
            return contestRepository.save(contest);
        }

        int winnerIndex = random.nextInt(participants.size());
        String winnerId = participants.get(winnerIndex);

        Optional<User> winnerOpt = userRepository.findById(winnerId);
        if (winnerOpt.isPresent()) {
            User winner = winnerOpt.get();

            // 1. Przedłużenie / przyznanie karnetu
            membershipService.renewMembership(winner.getEmail(), contest.getDurationMonths());

            // 2. Powiadomienie email przez EmailJS
            emailJsService.sendContestWonEmail(
                    winner.getEmail(),
                    winner.getFirstName(),
                    contest.getTitle(),
                    contest.getDurationMonths()
            );

            contest.setWinnerUserId(winner.getId());
            contest.setWinnerUserName(winner.getFirstName() + " " + winner.getLastName());
            contest.setWinnerUserEmail(winner.getEmail());
            log.info("Wylosowano zwycięzcę konkursu '{}': {} ({})", contest.getTitle(), winner.getEmail(), winner.getFirstName());
        }

        contest.setStatus(ContestStatus.FINISHED);
        contest.setUpdatedAt(LocalDateTime.now());
        return contestRepository.save(contest);
    }

    public List<ContestDto> getAllContestsForUser(String userEmail) {
        User user = userRepository.findByEmail(userEmail).orElse(null);
        String currentUserId = user != null ? user.getId() : null;

        List<Contest> contests = contestRepository.findAllByOrderByCreatedAtDesc();
        return contests.stream().map(c -> mapToDto(c, currentUserId)).collect(Collectors.toList());
    }

    public List<ContestDto> getAllContestsForAdmin() {
        List<Contest> contests = contestRepository.findAllByOrderByCreatedAtDesc();
        return contests.stream().map(c -> mapToDto(c, null)).collect(Collectors.toList());
    }

    private ContestDto mapToDto(Contest c, String currentUserId) {
        boolean joined = currentUserId != null && c.getParticipantUserIds() != null && c.getParticipantUserIds().contains(currentUserId);
        int count = c.getParticipantUserIds() != null ? c.getParticipantUserIds().size() : 0;

        return ContestDto.builder()
                .id(c.getId())
                .title(c.getTitle())
                .description(c.getDescription())
                .membershipType(c.getMembershipType())
                .durationMonths(c.getDurationMonths())
                .startDate(c.getStartDate())
                .endDate(c.getEndDate())
                .status(c.getStatus())
                .participantCount(count)
                .isUserJoined(joined)
                .winnerUserId(c.getWinnerUserId())
                .winnerUserName(c.getWinnerUserName())
                .winnerUserEmail(c.getWinnerUserEmail())
                .createdAt(c.getCreatedAt())
                .build();
    }
}
