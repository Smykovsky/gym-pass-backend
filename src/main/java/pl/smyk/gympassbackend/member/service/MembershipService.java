package pl.smyk.gympassbackend.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.smyk.gympassbackend.auth.model.User;
import pl.smyk.gympassbackend.auth.service.AuthService;
import pl.smyk.gympassbackend.member.model.Membership;
import pl.smyk.gympassbackend.member.repository.MembershipRepository;
import pl.smyk.gympassbackend.member.util.AESUtils;
import pl.smyk.gympassbackend.member.util.CodeGenerator;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MembershipService {
    private final MembershipRepository membershipRepository;
    private final AuthService userService;
    private final CodeGenerator codeGenerator;
    private final AuthService authService;


    public Membership getMembershipByEmail(String email) {
        if (email.isEmpty()) {
            return null;
        }

        Optional<User> byEmail = userService.findByEmail(email);
        if (!byEmail.isPresent()) {
            return null;
        }

        User user = byEmail.get();
        return user.getMembership();
    }

    public void renewMembership(String userEmail, int months) {
        Optional<User> byEmail = userService.findByEmail(userEmail);
        if (byEmail.isEmpty()) {
            return;
        }

        User user = byEmail.get();
        assert user.getMembership() != null;
        Membership membership = user.getMembership();
        LocalDate start = LocalDate.now();
        LocalDate end = LocalDate.now().plusMonths(months);

        membership.setStartDate(start);
        membership.setEndDate(end);
        membership.setActive(true);
        membership.generateCodes(codeGenerator, user.getId());

        membershipRepository.save(user.getMembership());

        userService.save(user);
    }

    public Membership scanUserMembershipPass(String qrCodeData) throws Exception {
        if (qrCodeData.isEmpty()) {
            return null;
        }

        String decryptedData = AESUtils.decrypt(qrCodeData);

        Optional<User> byId = authService.findById(decryptedData);

        User user = byId.get();
        return user.getMembership();
    }

    //refactor
    public boolean isMembershipValid(Membership membership) {
        if (membership == null ) {
            return false;
        }

        LocalDate today = LocalDate.now();


        return !today.isBefore(membership.getStartDate()) && !today.isAfter(membership.getEndDate());
    }
}
