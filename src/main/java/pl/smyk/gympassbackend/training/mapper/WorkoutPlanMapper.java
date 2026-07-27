package pl.smyk.gympassbackend.training.mapper;

import pl.smyk.gympassbackend.training.dto.WorkoutDayDto;
import pl.smyk.gympassbackend.training.dto.WorkoutExerciseDto;
import pl.smyk.gympassbackend.training.dto.WorkoutPlanDto;
import pl.smyk.gympassbackend.training.model.WorkoutDay;
import pl.smyk.gympassbackend.training.model.WorkoutExercise;
import pl.smyk.gympassbackend.training.model.WorkoutPlan;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class WorkoutPlanMapper {

    public static WorkoutPlanDto toDto(WorkoutPlan entity) {
        if (entity == null) return null;

        WorkoutPlanDto.WorkoutPlanDtoBuilder builder = WorkoutPlanDto.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .level(entity.getLevel())
                .status(entity.getStatus())
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt());

        if (entity.getTrainer() != null) {
            builder.trainerId(entity.getTrainer().getId())
                    .trainerEmail(entity.getTrainer().getEmail())
                    .trainerName(entity.getTrainer().getFirstName() + " " + entity.getTrainer().getLastName());
        }

        if (entity.getAssignedUser() != null) {
            builder.assignedUserId(entity.getAssignedUser().getId())
                    .assignedUserEmail(entity.getAssignedUser().getEmail())
                    .assignedUserName(entity.getAssignedUser().getFirstName() + " " + entity.getAssignedUser().getLastName());
        }

        if (entity.getDays() != null) {
            builder.days(entity.getDays().stream().map(WorkoutPlanMapper::toDayDto).collect(Collectors.toList()));
        } else {
            builder.days(new ArrayList<>());
        }

        return builder.build();
    }

    public static WorkoutDayDto toDayDto(WorkoutDay day) {
        if (day == null) return null;
        List<WorkoutExerciseDto> exerciseDtos = new ArrayList<>();
        if (day.getExercises() != null) {
            exerciseDtos = day.getExercises().stream().map(WorkoutPlanMapper::toExerciseDto).collect(Collectors.toList());
        }
        return WorkoutDayDto.builder()
                .dayName(day.getDayName())
                .dayOrder(day.getDayOrder())
                .exercises(exerciseDtos)
                .build();
    }

    public static WorkoutExerciseDto toExerciseDto(WorkoutExercise exercise) {
        if (exercise == null) return null;
        return WorkoutExerciseDto.builder()
                .exerciseName(exercise.getExerciseName())
                .muscleGroup(exercise.getMuscleGroup())
                .sets(exercise.getSets())
                .reps(exercise.getReps())
                .targetWeight(exercise.getTargetWeight())
                .restTimeSeconds(exercise.getRestTimeSeconds())
                .trainerNotes(exercise.getTrainerNotes())
                .build();
    }

    public static WorkoutDay toDayEntity(WorkoutDayDto dto) {
        if (dto == null) return null;
        List<WorkoutExercise> exercises = new ArrayList<>();
        if (dto.getExercises() != null) {
            exercises = dto.getExercises().stream().map(WorkoutPlanMapper::toExerciseEntity).collect(Collectors.toList());
        }
        return WorkoutDay.builder()
                .dayName(dto.getDayName())
                .dayOrder(dto.getDayOrder())
                .exercises(exercises)
                .build();
    }

    public static WorkoutExercise toExerciseEntity(WorkoutExerciseDto dto) {
        if (dto == null) return null;
        return WorkoutExercise.builder()
                .exerciseName(dto.getExerciseName())
                .muscleGroup(dto.getMuscleGroup())
                .sets(dto.getSets())
                .reps(dto.getReps())
                .targetWeight(dto.getTargetWeight())
                .restTimeSeconds(dto.getRestTimeSeconds())
                .trainerNotes(dto.getTrainerNotes())
                .build();
    }
}
