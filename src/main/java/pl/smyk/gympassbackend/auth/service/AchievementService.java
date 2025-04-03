package pl.smyk.gympassbackend.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.smyk.gympassbackend.auth.model.Achievement;
import pl.smyk.gympassbackend.auth.model.User;
import pl.smyk.gympassbackend.auth.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class AchievementService {
    private final UserRepository userRepository;

    public void updateTrainingsAchievement(User user) {
        if (!user.isCompleted(Achievement.FIRST_TRAINING)) {
            user.incrementProgress(Achievement.FIRST_TRAINING, 1);
        }

        if (!user.isCompleted(Achievement.TEN_TRAININGS)) {
            user.incrementProgress(Achievement.TEN_TRAININGS, 1);
        }

        userRepository.save(user);
    }

    public void updateLoginAchievements(User user) {
        if (!user.isCompleted(Achievement.FIRST_LOGIN)) {
            user.incrementProgress(Achievement.FIRST_LOGIN, 1);
        }

        if (!user.isCompleted(Achievement.HUNDRED_LOGINS)) {
            user.incrementProgress(Achievement.HUNDRED_LOGINS, 1);
        }

        userRepository.save(user);
    }
}
