package pl.smyk.gympassbackend.member.dto;

import lombok.Data;

@Data
public class UserEntriesResponse {
    private int summaryEntries;
    private int monthlyEntries;
}
