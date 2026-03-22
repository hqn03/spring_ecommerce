package github.hqn03.auth_service.user.entity;

import github.hqn03.auth_service.auth.entity.Role;
import github.hqn03.auth_service.common.entity.BaseEntity;
import github.hqn03.auth_service.order.entity.Order;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Entity
@Table(name = "users")
@NoArgsConstructor
@Getter
@Setter
@Builder
@AllArgsConstructor
@SQLRestriction("deleted_at = 0")
@SQLDelete(sql = "UPDATE users SET deleted_at = UNIX_TIMESTAMP(NOW(3)) * 1000 WHERE id = ?")
public class User extends BaseEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    private String fullName;

    private String address;

    private String phone;

    @Column(nullable = false)
    private String email;

    @Column(unique = false, nullable = false)
    private String password;

    @Column(nullable = false)
    private boolean enabled = false;

    @Column(nullable = false)
    private boolean blocked = false;

    @ManyToMany(fetch = FetchType.LAZY)
    @BatchSize(size = 20)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "role_id", nullable = false)
    )
    private Set<Role> roles = new HashSet<>();

    @OneToMany(fetch =  FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Order> orders = new ArrayList<>();

    @Column(nullable = false)
    private Long deletedAt = 0L;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Set<GrantedAuthority> authorities = new HashSet<>();

        this.roles.forEach(role -> {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));
            role.getPermissions().forEach(permission -> {
                authorities.add(new SimpleGrantedAuthority(permission.getName()));
            });
        });

        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !this.blocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    public void addRole(Role role) {
        this.roles.add(role);
    }
}
