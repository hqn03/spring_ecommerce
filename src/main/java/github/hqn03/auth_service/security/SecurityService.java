package github.hqn03.auth_service.security;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SecurityService {

    private Authentication getAuth() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public Long getUserId() {
        Authentication auth = getAuth();
        if(auth instanceof AnonymousAuthenticationToken){
            return null;
        }
        return Long.valueOf(auth.getPrincipal().toString());
    }

    public Set<String> getScopes() {
        Authentication auth = getAuth();
        if (auth == null) return Set.of();

        return getAuth().getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());
    }

    public boolean isSuperAdmin() {
        Authentication auth = getAuth();
        if (auth == null) return false;

        return getAuth().getAuthorities()
                .stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_SUPER_ADMIN"));
    }
}
