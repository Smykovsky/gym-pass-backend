package pl.smyk.gympassbackend.gymPass.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.smyk.gympassbackend.gymPass.model.Membership;
import pl.smyk.gympassbackend.gymPass.repository.MembershipRepository;

@Service
@RequiredArgsConstructor
public class MembershipService {
    private final MembershipRepository membershipRepository;

    public void addMembership() {

    }
}
