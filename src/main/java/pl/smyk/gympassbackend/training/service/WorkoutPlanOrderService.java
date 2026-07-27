package pl.smyk.gympassbackend.training.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.smyk.gympassbackend.auth.exception.UserNotFoundException;
import pl.smyk.gympassbackend.auth.model.Trainer;
import pl.smyk.gympassbackend.auth.model.User;
import pl.smyk.gympassbackend.auth.repository.TrainerRepository;
import pl.smyk.gympassbackend.auth.repository.UserRepository;
import pl.smyk.gympassbackend.training.dto.*;
import pl.smyk.gympassbackend.training.mapper.WorkoutPlanMapper;
import pl.smyk.gympassbackend.training.model.*;
import pl.smyk.gympassbackend.training.repository.WorkoutPlanOrderRepository;
import pl.smyk.gympassbackend.training.repository.WorkoutPlanRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import pl.smyk.gympassbackend.common.service.EmailJsService;
import pl.smyk.gympassbackend.member.service.PaymentService;

@Service
@RequiredArgsConstructor
public class WorkoutPlanOrderService {
    private final WorkoutPlanOrderRepository orderRepository;
    private final UserRepository userRepository;
    private final TrainerRepository trainerRepository;
    private final WorkoutPlanRepository workoutPlanRepository;
    private final WorkoutPlanService workoutPlanService;
    private final PaymentService paymentService;
    private final EmailJsService emailJsService;

    public WorkoutPlanOrderDto createOrder(String userEmail, CreateWorkoutPlanOrderRequest request) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("Nie znaleziono użytkownika!"));

        Trainer trainer = trainerRepository.findById(request.getTrainerId())
                .orElseThrow(() -> new UserNotFoundException("Nie znaleziono trenera!"));

        Double price = request.getPrice() != null ? request.getPrice() : 99.00;
        LocalDateTime now = LocalDateTime.now();

        WorkoutPlanOrder order = WorkoutPlanOrder.builder()
                .user(user)
                .trainer(trainer)
                .price(price)
                .status(OrderStatus.PENDING_PAYMENT)
                .questionnaire(request.getQuestionnaire())
                .createdAt(now)
                .updatedAt(now)
                .build();

        WorkoutPlanOrder saved = orderRepository.save(order);
        return mapToDto(saved);
    }

    public WorkoutPlanOrderDto payOrder(String userEmail, String orderId, PayWorkoutPlanOrderRequest request) {
        WorkoutPlanOrder order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono zamówienia!"));

        try {
            String stripeResult = paymentService.processStripeOrderPayment(
                    userEmail,
                    order.getPrice(),
                    order.getId(),
                    request.getBlikCode()
            );

            if ("blik_code_error".equals(stripeResult)) {
                throw new IllegalArgumentException("Nieprawidłowy kod BLIK! Podaj poprawny 6-cyfrowy kod BLIK (kod testowy: 123456).");
            }
        } catch (Exception e) {
            if (e instanceof IllegalArgumentException) {
                throw (IllegalArgumentException) e;
            }
            throw new RuntimeException("Błąd podczas autoryzacji płatności ze Stripe: " + e.getMessage());
        }

        order.setStatus(OrderStatus.PAID_PENDING_TRAINER);
        order.setUpdatedAt(LocalDateTime.now());

        WorkoutPlanOrder saved = orderRepository.save(order);

        // Wysyłka powiadomienia email przez EmailJS
        emailJsService.sendOrderPaidEmail(
                userEmail,
                saved.getUser() != null ? saved.getUser().getFirstName() : "Klubowicz",
                saved.getTrainer() != null ? saved.getTrainer().getFirstName() + " " + saved.getTrainer().getLastName() : "Trener",
                saved.getId(),
                saved.getQuestionnaire() != null ? saved.getQuestionnaire().getGoal() : ""
        );

        return mapToDto(saved);
    }

    public List<WorkoutPlanOrderDto> getUserOrders(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("Nie znaleziono użytkownika!"));

        List<WorkoutPlanOrder> orders = orderRepository.findAllByUserIdOrderByCreatedAtDesc(user.getId());
        return orders.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    public List<WorkoutPlanOrderDto> getTrainerOrders(String trainerEmail) {
        Trainer trainer = trainerRepository.findByEmail(trainerEmail)
                .orElseThrow(() -> new UserNotFoundException("Nie znaleziono trenera!"));

        List<WorkoutPlanOrder> orders = orderRepository.findAllByTrainerIdOrderByCreatedAtDesc(trainer.getId());
        return orders.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    public WorkoutPlanOrderDto fulfillOrder(String trainerEmail, String orderId, CreateWorkoutPlanRequest planRequest) {
        WorkoutPlanOrder order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono zamówienia!"));

        Trainer trainer = trainerRepository.findByEmail(trainerEmail)
                .orElseThrow(() -> new UserNotFoundException("Nie znaleziono trenera!"));

        planRequest.setAssignedUserId(order.getUser().getId());
        planRequest.setStatus(WorkoutStatus.ACTIVE);

        WorkoutPlanDto createdPlan = workoutPlanService.createWorkoutPlan(trainerEmail, planRequest);
        WorkoutPlan planEntity = workoutPlanRepository.findById(createdPlan.getId()).orElse(null);

        order.setWorkoutPlan(planEntity);
        order.setStatus(OrderStatus.FULFILLED);
        order.setUpdatedAt(LocalDateTime.now());

        WorkoutPlanOrder saved = orderRepository.save(order);

        // Wysyłka powiadomienia email o zrealizowanym planie przez EmailJS
        emailJsService.sendPlanFulfilledEmail(
                order.getUser().getEmail(),
                order.getUser().getFirstName(),
                planRequest.getTitle()
        );

        return mapToDto(saved);
    }

    public WorkoutPlanOrderDto mapToDto(WorkoutPlanOrder order) {
        if (order == null) return null;

        WorkoutPlanOrderDto.WorkoutPlanOrderDtoBuilder builder = WorkoutPlanOrderDto.builder()
                .id(order.getId())
                .price(order.getPrice())
                .status(order.getStatus())
                .questionnaire(order.getQuestionnaire())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt());

        if (order.getUser() != null) {
            builder.userId(order.getUser().getId())
                    .userEmail(order.getUser().getEmail())
                    .userName(order.getUser().getFirstName() + " " + order.getUser().getLastName());
        }

        if (order.getTrainer() != null) {
            builder.trainerId(order.getTrainer().getId())
                    .trainerEmail(order.getTrainer().getEmail())
                    .trainerName(order.getTrainer().getFirstName() + " " + order.getTrainer().getLastName());
        }

        if (order.getWorkoutPlan() != null) {
            builder.workoutPlan(WorkoutPlanMapper.toDto(order.getWorkoutPlan()));
        }

        return builder.build();
    }
}
