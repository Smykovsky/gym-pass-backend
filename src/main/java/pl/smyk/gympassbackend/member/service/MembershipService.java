package pl.smyk.gympassbackend.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.smyk.gympassbackend.auth.model.User;
import pl.smyk.gympassbackend.auth.service.AuthService;
import pl.smyk.gympassbackend.member.dto.MembershipDto;
import pl.smyk.gympassbackend.member.dto.ValidateMemberResponse;
import pl.smyk.gympassbackend.member.mapper.MembershipMapper;
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


    public MembershipDto getMembershipByEmail(String email) {
        if (email.isEmpty()) {
            return null;
        }

        Optional<User> byEmail = userService.findByEmail(email);
        if (!byEmail.isPresent()) {
            return null;
        }

        User user = byEmail.get();
        return MembershipMapper.INSTANCE.toDto(user.getMembership());
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
//        membership.setActive(true);
        membership.generateCodes(codeGenerator, user.getId());

        membershipRepository.save(user.getMembership());

        userService.save(user);
    }

    public ValidateMemberResponse scanUserMembershipPass(String qrCodeData) throws Exception {
        if (qrCodeData.isEmpty()) {
            return null;
        }

        String decryptedData = AESUtils.decrypt(qrCodeData);

        Optional<User> byId = authService.findById(decryptedData);

        User user = byId.get();

//        boolean membershipValid = isMembershipValid(user.getMembership());

        ValidateMemberResponse response = new ValidateMemberResponse();
        response.setUserEmail(user.getEmail());
        response.setMembershipCardId(user.getMembership().getId());
        System.out.println(user.getMembership().isActive());
        response.setValid(user.getMembership().isActive());

        return response;

    }

    public boolean isMembershipValid(Membership membership) {
        if (membership == null ) {
            return false;
        }

        LocalDate today = LocalDate.now();

        if (membership.getStartDate() == null || membership.getEndDate() == null) {
            return false;
        }


        return !today.isBefore(membership.getStartDate()) && !today.isAfter(membership.getEndDate());
    }
}
