package rs.delimo.user.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import rs.delimo.common.valueobject.UserId;

import java.util.Collection;
import java.util.List;
import java.util.UUID;


@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")

public class User implements UserDetails {
    @EmbeddedId
    @Builder.Default
    private UserId id = UserId.generate();
    @Email
    @Column(unique = true)
    private String email;
    private String name;
    private String password;
    private String city;
    private String street;
    private String phone;
    private String viber;
    @Enumerated(EnumType.STRING)
    private Role role;
    private Boolean enabled;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(this.role.toString()));
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    @PrePersist
    private void ensureId() {
        if (this.id == null) {
            this.id = new UserId(UUID.randomUUID());
        }
    }
}
