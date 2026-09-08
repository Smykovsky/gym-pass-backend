package pl.smyk.gympassbackend.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.smyk.gympassbackend.member.dto.MembershipPlanRequest;
import pl.smyk.gympassbackend.member.model.MembershipPlan;
import pl.smyk.gympassbackend.member.repository.MembershipPlanRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MembershipPlanService {

    private final MembershipPlanRepository membershipPlanRepository;

    /** Plans offered for sale, ordered for display. */
    public List<MembershipPlan> getActivePlans() {
        return membershipPlanRepository.findAllByActiveTrueOrderBySortOrderAscMonthsAsc();
    }

    /** Every plan, including inactive ones (admin view). */
    public List<MembershipPlan> getAllPlans() {
        return membershipPlanRepository.findAllByOrderBySortOrderAscMonthsAsc();
    }

    public MembershipPlan getById(String id) {
        return membershipPlanRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono planu karnetu o ID: " + id));
    }

    public MembershipPlan getByCode(String code) {
        return membershipPlanRepository.findByCode(code)
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono planu karnetu o kodzie: " + code));
    }

    /** Resolve a plan by code, falling back to any active plan when the code is unknown/blank. */
    public MembershipPlan getByCodeOrDefault(String code) {
        if (code != null && !code.isBlank()) {
            return membershipPlanRepository.findByCode(code)
                    .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono planu karnetu o kodzie: " + code));
        }
        return getActivePlans().stream().findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Brak zdefiniowanych planów karnetów."));
    }

    public MembershipPlan create(MembershipPlanRequest request) {
        String code = normalizeCode(request.getCode());
        if (code.isBlank()) {
            throw new IllegalArgumentException("Kod planu jest wymagany.");
        }
        if (membershipPlanRepository.existsByCode(code)) {
            throw new IllegalArgumentException("Plan karnetu o kodzie '" + code + "' już istnieje.");
        }

        MembershipPlan plan = MembershipPlan.builder()
                .code(code)
                .createdAt(Instant.now())
                .build();
        applyRequest(plan, request);
        plan.setUpdatedAt(Instant.now());

        MembershipPlan saved = membershipPlanRepository.save(plan);
        log.info("Utworzono plan karnetu '{}' (kod: {})", saved.getName(), saved.getCode());
        return saved;
    }

    public MembershipPlan update(String id, MembershipPlanRequest request) {
        MembershipPlan plan = getById(id);

        if (request.getCode() != null && !request.getCode().isBlank()) {
            String newCode = normalizeCode(request.getCode());
            if (!newCode.equals(plan.getCode()) && membershipPlanRepository.existsByCode(newCode)) {
                throw new IllegalArgumentException("Plan karnetu o kodzie '" + newCode + "' już istnieje.");
            }
            plan.setCode(newCode);
        }

        applyRequest(plan, request);
        plan.setUpdatedAt(Instant.now());

        MembershipPlan saved = membershipPlanRepository.save(plan);
        log.info("Zaktualizowano plan karnetu '{}' (kod: {})", saved.getName(), saved.getCode());
        return saved;
    }

    public void delete(String id) {
        MembershipPlan plan = getById(id);
        membershipPlanRepository.delete(plan);
        log.info("Usunięto plan karnetu '{}' (kod: {})", plan.getName(), plan.getCode());
    }

    private void applyRequest(MembershipPlan plan, MembershipPlanRequest request) {
        if (request.getName() != null) {
            plan.setName(request.getName().trim());
        }
        if (request.getMonths() != null) {
            if (request.getMonths() < 1) {
                throw new IllegalArgumentException("Liczba miesięcy musi być dodatnia.");
            }
            plan.setMonths(request.getMonths());
        }
        if (request.getBasePrice() != null) {
            if (request.getBasePrice().signum() < 0) {
                throw new IllegalArgumentException("Cena bazowa nie może być ujemna.");
            }
            plan.setBasePrice(request.getBasePrice().setScale(2, RoundingMode.HALF_UP));
        }
        if (request.getDiscountPercent() != null) {
            plan.setDiscountPercent(clampPercent(request.getDiscountPercent()));
        }
        if (request.getActive() != null) {
            plan.setActive(request.getActive());
        }
        if (request.getSortOrder() != null) {
            plan.setSortOrder(request.getSortOrder());
        }

        if (request.getPromotion() != null) {
            MembershipPlanRequest.PromotionRequest pr = request.getPromotion();
            MembershipPlan.Promotion promo = plan.getPromotion() != null
                    ? plan.getPromotion()
                    : MembershipPlan.Promotion.builder().build();

            if (pr.getEnabled() != null) promo.setEnabled(pr.getEnabled());
            if (pr.getLabel() != null) promo.setLabel(pr.getLabel().trim());
            if (pr.getDiscountPercent() != null) promo.setDiscountPercent(clampPercent(pr.getDiscountPercent()));
            promo.setStartsAt(pr.getStartsAt());
            promo.setEndsAt(pr.getEndsAt());

            if (promo.isEnabled() && promo.getStartsAt() != null && promo.getEndsAt() != null
                    && promo.getEndsAt().isBefore(promo.getStartsAt())) {
                throw new IllegalArgumentException("Data zakończenia promocji nie może być wcześniejsza niż data rozpoczęcia.");
            }

            boolean promoEmpty = !promo.isEnabled()
                    && (promo.getLabel() == null || promo.getLabel().isBlank())
                    && promo.getDiscountPercent() == 0
                    && promo.getStartsAt() == null && promo.getEndsAt() == null;
            plan.setPromotion(promoEmpty ? null : promo);
        }

        if (plan.getName() == null || plan.getName().isBlank()) {
            throw new IllegalArgumentException("Nazwa planu jest wymagana.");
        }
        if (plan.getMonths() < 1) {
            throw new IllegalArgumentException("Liczba miesięcy musi być dodatnia.");
        }
        if (plan.getBasePrice() == null) {
            throw new IllegalArgumentException("Cena bazowa jest wymagana.");
        }
    }

    private String normalizeCode(String code) {
        return code == null ? "" : code.trim().toUpperCase().replaceAll("[^A-Z0-9_]", "_");
    }

    private int clampPercent(int value) {
        return Math.max(0, Math.min(90, value));
    }
}
