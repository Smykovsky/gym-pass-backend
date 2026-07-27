package pl.smyk.gympassbackend.auth.model;

import jakarta.annotation.Nullable;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import pl.smyk.gympassbackend.member.model.Membership;

import java.util.*;

@Document(collection = "users")
@TypeAlias("user")
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User implements UserDetails {
    @MongoId
    private String id;
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private Gender gender;
    private List<Role> roles;
    @DBRef
    @Nullable
    private Membership membership;
    private Map<Achievement, Integer> progress = new HashMap<>();
    @Nullable
    private String profileImage;
    @Builder.Default
    private boolean accountNonLocked = true;

    public void incrementProgress(Achievement achievement, int value) {
        int newProgress = progress.getOrDefault(achievement, 0) + value;
        progress.put(achievement, newProgress);

        if (newProgress >= achievement.getRequiredProgress() && !isCompleted(achievement)) {
            progress.put(achievement, achievement.getRequiredProgress());
        }
    }

    public int getProgress(Achievement achievement) {
        return progress.getOrDefault(achievement, 0);
    }

    public boolean isCompleted(Achievement achievement) {
        return progress.getOrDefault(achievement, 0) >= achievement.getRequiredProgress();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for (Role role : roles) {
            authorities.addAll(role.getAuthorities());
        }
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

}
