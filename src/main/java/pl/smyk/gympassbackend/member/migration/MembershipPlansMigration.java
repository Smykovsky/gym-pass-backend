package pl.smyk.gympassbackend.member.migration;

import io.mongock.api.annotations.*;
import lombok.RequiredArgsConstructor;
import pl.smyk.gympassbackend.member.model.MembershipPlan;
import pl.smyk.gympassbackend.member.repository.MembershipPlanRepository;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@ChangeUnit(order = "100", id = "membership-plans-seeder", author = "dev")
@RequiredArgsConstructor
public class MembershipPlansMigration {

    private final MembershipPlanRepository membershipPlanRepository;

    private static final BigDecimal PRICE_PER_MONTH = new BigDecimal("100.00");

    @BeforeExecution
    public void before() {
        System.out.println("Before seeding membership plans...");
    }

    @RollbackBeforeExecution
    public void rollbackBefore() {
        System.out.println("Rollback before seeding membership plans...");
    }

    @Execution
    public void seed() {
        List<PlanSeed> seeds = List.of(
                new PlanSeed("MONTHLY", "Karnet miesięczny", 1, 0, 1),
                new PlanSeed("QUARTERLY", "Karnet kwartalny", 3, 10, 2),
                new PlanSeed("SEMI_ANNUAL", "Karnet półroczny", 6, 20, 3),
                new PlanSeed("ANNUAL", "Karnet roczny", 12, 35, 4)
        );

        Instant now = Instant.now();
        for (PlanSeed s : seeds) {
            if (membershipPlanRepository.existsByCode(s.code())) {
                continue;
            }
            MembershipPlan plan = MembershipPlan.builder()
                    .code(s.code())
                    .name(s.name())
                    .months(s.months())
                    .basePrice(PRICE_PER_MONTH.multiply(BigDecimal.valueOf(s.months())))
                    .discountPercent(s.discountPercent())
                    .active(true)
                    .sortOrder(s.sortOrder())
                    .createdAt(now)
                    .updatedAt(now)
                    .build();
            membershipPlanRepository.save(plan);
        }
    }

    @RollbackExecution
    public void rollback() {
        System.out.println("Rollback membership plans seeder...");
        List.of("MONTHLY", "QUARTERLY", "SEMI_ANNUAL", "ANNUAL")
                .forEach(code -> membershipPlanRepository.findByCode(code)
                        .ifPresent(membershipPlanRepository::delete));
    }

    private record PlanSeed(String code, String name, int months, int discountPercent, int sortOrder) {
    }
}
