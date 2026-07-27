package pl.smyk.gympassbackend.training.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pl.smyk.gympassbackend.training.dto.CreateWorkoutPlanOrderRequest;
import pl.smyk.gympassbackend.training.dto.CreateWorkoutPlanRequest;
import pl.smyk.gympassbackend.training.dto.PayWorkoutPlanOrderRequest;
import pl.smyk.gympassbackend.training.dto.WorkoutPlanOrderDto;
import pl.smyk.gympassbackend.training.service.WorkoutPlanOrderService;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class WorkoutPlanOrderController {
    private final WorkoutPlanOrderService orderService;

    // USER ENDPOINTS
    @PostMapping("/user/workout-plan-orders")
    public ResponseEntity<WorkoutPlanOrderDto> createOrder(
            Authentication authentication,
            @RequestBody CreateWorkoutPlanOrderRequest request
    ) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }
        WorkoutPlanOrderDto order = orderService.createOrder(authentication.getName(), request);
        return ResponseEntity.ok(order);
    }

    @PostMapping("/user/workout-plan-orders/{orderId}/pay")
    public ResponseEntity<WorkoutPlanOrderDto> payOrder(
            Authentication authentication,
            @PathVariable String orderId,
            @RequestBody PayWorkoutPlanOrderRequest request
    ) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }
        WorkoutPlanOrderDto paid = orderService.payOrder(authentication.getName(), orderId, request);
        return ResponseEntity.ok(paid);
    }

    @GetMapping("/user/workout-plan-orders")
    public ResponseEntity<List<WorkoutPlanOrderDto>> getUserOrders(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }
        List<WorkoutPlanOrderDto> orders = orderService.getUserOrders(authentication.getName());
        return ResponseEntity.ok(orders);
    }

    // TRAINER ENDPOINTS
    @GetMapping("/trainer/workout-plan-orders")
    public ResponseEntity<List<WorkoutPlanOrderDto>> getTrainerOrders(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }
        List<WorkoutPlanOrderDto> orders = orderService.getTrainerOrders(authentication.getName());
        return ResponseEntity.ok(orders);
    }

    @PostMapping("/trainer/workout-plan-orders/{orderId}/fulfill")
    public ResponseEntity<WorkoutPlanOrderDto> fulfillOrder(
            Authentication authentication,
            @PathVariable String orderId,
            @RequestBody CreateWorkoutPlanRequest planRequest
    ) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }
        WorkoutPlanOrderDto fulfilled = orderService.fulfillOrder(authentication.getName(), orderId, planRequest);
        return ResponseEntity.ok(fulfilled);
    }
}
