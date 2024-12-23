package fr.mossaab.security.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import fr.mossaab.security.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.List;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "_user")
public class User implements UserDetails {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Column(nullable = false, unique = true)
    private String email;

    private String password;

    private String activationCode;

    @Column(nullable = false, columnDefinition = "bigint default 0")
    private Integer pears = 0; // Количество груш пользователя (может быть null)

    @Column(nullable = false, columnDefinition = "bigint default 0")
    private Integer points = 0; // Баллы пользователя (может быть null)

    @OneToOne(mappedBy = "user", cascade = CascadeType.MERGE, orphanRemoval = true)
    @JsonManagedReference
    private FileData fileData;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<RefreshToken> refreshTokens;
    @Embedded
    private ProposedChanges proposedChanges;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.getAuthorities();
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
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
