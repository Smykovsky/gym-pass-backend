package pl.smyk.gympassbackend.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.smyk.gympassbackend.member.dto.UserEntriesResponse;
import pl.smyk.gympassbackend.member.model.Entry;
import pl.smyk.gympassbackend.member.repository.EntryRepository;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EntryService {
    private final EntryRepository entryRepository;

    public UserEntriesResponse findAllUserEntries(String userEmail) {
        List<Entry> allUserEntries = entryRepository.findAllByUserEmail(userEmail);

        int summaryEntries = allUserEntries.size();

        LocalDateTime now = LocalDateTime.now();
        int currentYear = now.getYear();
        Month currentMonth = now.getMonth();

        List<Entry> monthlyEntries = allUserEntries.stream()
                .filter(entry -> entry.getEntryDate().getYear() == currentYear &&
                        entry.getEntryDate().getMonth() == currentMonth)
                .toList();

        int monthlyEntriesCount = monthlyEntries.size();

        UserEntriesResponse response = new UserEntriesResponse();
        response.setSummaryEntries(summaryEntries);
        response.setMonthlyEntries(monthlyEntriesCount);

        return response;
    }

    public void addUserEntry(String userEmail, String membershipCardId) {
        if (userEmail == null) {
            return;
        }
        Entry newEntry = Entry.builder().userEmail(userEmail).membershipCardId(membershipCardId).entryDate(LocalDateTime.now()).build();

        entryRepository.save(newEntry);
    }
}
