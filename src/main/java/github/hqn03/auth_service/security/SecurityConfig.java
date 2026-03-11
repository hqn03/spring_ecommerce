package github.hqn03.auth_service.security;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import github.hqn03.auth_service.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.UUID;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return identifier -> userRepository.findByUsernameOrEmail(identifier,identifier)
                .orElseThrow(() -> new UsernameNotFoundException("Invalid identifier"));
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, CustomAuthenticationEntryPoint customAuthenticationEntryPoint) throws Exception {
        return http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**",
                                "/swagger-ui/**",
                                "/v3/api-docs/**").permitAll()
                        .requestMatchers("/api/permissions").hasAuthority("SCOPE_ROLE:CREATE")
                        .anyRequest().authenticated())
                .oauth2ResourceServer(oauth -> oauth
                        .authenticationEntryPoint(customAuthenticationEntryPoint)
                        .jwt(Customizer.withDefaults()))

                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtEncoder jwtEncoder(JWKSource<SecurityContext> jwkSource) {
        return new NimbusJwtEncoder(jwkSource);
    }

    @Bean
    public JwtDecoder jwtDecoder(RSAKey rsaKey) throws Exception {
        return NimbusJwtDecoder.withPublicKey(rsaKey.toRSAPublicKey()).build();
    }

    @Bean
    public JWKSource<SecurityContext> jwkSource() {
        RSAKey rsaKey = generateRsa();
        JWKSet jwkSet = new JWKSet(rsaKey);
        return (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);
    }

    @Bean
    public RSAKey generateRsa() {

        //Dev key
        String devPublicKeyStr = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAhnIGylkByYehf9miwoJI" +
                "HywUTQdU40EfgkoHl6ZsXkaMMlSE73M3Yvg0ExrxRzeDYam9hX9Ai2v1TTgvMuj5" +
                "hjmWCK9QfpD5PV3FV2O6NVzNVVrR9c6TZZGx5Sux+x/LmaDOR2x2gdktBy8uTCy0" +
                "NhJ0wFx7PpqGUyqkM8RoplhJ9Z59oyVw+ZPxIo9UngwKRQTx+WOC2nVlHqaaUR25" +
                "wCul6jqv9NihjA02iv+4nPY49iAbUwpNICeOyYi8/ifWjpR9AaOi0nPqkjd/THxh" +
                "B55Xo+l1O//N3mXb0qNRMVAKwV37H2EPl5Ta5ISiOiqaiSMeUpHSzywRdebyVmZ3" +
                "WwIDAQAB";
        String devPrivateKeyStr = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCGcgbKWQHJh6F/" +
                "2aLCgkgfLBRNB1TjQR+CSgeXpmxeRowyVITvczdi+DQTGvFHN4Nhqb2Ff0CLa/VN" +
                "OC8y6PmGOZYIr1B+kPk9XcVXY7o1XM1VWtH1zpNlkbHlK7H7H8uZoM5HbHaB2S0H" +
                "Ly5MLLQ2EnTAXHs+moZTKqQzxGimWEn1nn2jJXD5k/Eij1SeDApFBPH5Y4LadWUe" +
                "pppRHbnAK6XqOq/02KGMDTaK/7ic9jj2IBtTCk0gJ47JiLz+J9aOlH0Bo6LSc+qS" +
                "N39MfGEHnlej6XU7/83eZdvSo1ExUArBXfsfYQ+XlNrkhKI6KpqJIx5SkdLPLBF1" +
                "5vJWZndbAgMBAAECggEBAIN9yFhlPxM8vN5PHBmC1EjwkpZXwlVVXxzNjKlsUnbO" +
                "JtYexblvhU8dk8jPxAU0LSH6omqLb7QsjUjN15PCOjFl5vBtjpuK7MlxCc4Tso34" +
                "SrbdUZZISO7A1S1fU/5ZNYb2uGkwkqMipiBtq6lC26o62Mc+bCdpeFCk1t3uja+K" +
                "GvuVsOS9I6ifwZ13cXDJ7xW0diEuCDFy6YAJmQGZgVyZFGmiCJ2fgR8BsBugEznR" +
                "T78ov7UygtT07csSMddHNxHVGVIKof0d51IaYClw6XgLfGbdulP9/c8dGsjgL4cm" +
                "8HiOMftkj1Zggqk2lD7ntETtooltSbD1Rt3QhNotP1ECgYEA6zLGlqmjJF1Kzey8" +
                "oQWg5b2UMUEV2ltZAsIHGycavHrKdbJrIqFJsTTbTmyp5kCEpMHXRNsmr3pRluav" +
                "5zGtu0HcXKge5o9NyEkoXAsLH1LpCK7o8lO3XgjFrYCTb3RL87kIjm1mg0u8sLGQ" +
                "Hhlf1hW6Jrgad/3VNEm88Be51ykCgYEAklYPwXQrwsW4orZM0yeVg9WFGUBN+OOF" +
                "s2g8rqzhGMwAeIyuKmjhra7/poI5Himjdgg2nSnTmZeSBTGm0Lh1hzdHcMd64t5o" +
                "+pbrGGSQnAh9S6gZGOnCarGf5tpbB8BaYXUwpnICbd3cFS8aoOcNQQDmZCiafzzQ" +
                "A1abMc5S/uMCgYAkCAtUWXicwY4MLhBXur0DdR+WzBnE8emY+pCMIFmANm99yG+R" +
                "RpQ7iZVvVkZQ9+FaPDf2XHx4tYx6Saz5BitPXK+dokPZnmqUHGEKeMajbdg1dFZL" +
                "iXe1+DIQenobwP1BwT8GUsw5oQivKQSjF9AOxBVYiSvE5iYbdJB1V60owQKBgHsE" +
                "GAijlSoONvGp2JllXF5QP6yDj0icU8gG54Rb1nqW/ApbEQqb0qPESit6q3N8w2o0" +
                "61xhTf1/GoR9QeOSPVKnGMppFxfiqlOGTSnbWGmjBVl1fWuhDwvMOETD6ORyQkhN" +
                "UNGLZBlh+WJhj7wVn+G2VPd73qz5vVAhHSPYc5LLAoGAND53QXQpSPxqHPJ3Cujn" +
                "5dfHSw0qqswTg4K5+KnPmaev03sBreBuavPB8VbRFEB0KxcldVQ58DehBF+1Lgn+" +
                "Lc9X1iIsjVtnzYCqQZ2nH84Et7dMoRFzdvX0Si9a4vGK5qQ2uQT1CTAQ4tDQ8RVK" +
                "UGtMmXf+al0uSDBpRmGNjjY=";

        try {
//            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
//            keyPairGenerator.initialize(2048);
//
//            KeyPair keyPair = keyPairGenerator.generateKeyPair();
//
//            RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
//            RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
//
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");

            // 2. Parse Public Key
            byte[] publicKeyBytes = Base64.getDecoder().decode(devPublicKeyStr);
            X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
            RSAPublicKey publicKey = (RSAPublicKey) keyFactory.generatePublic(publicKeySpec);

            // 3. Parse Private Key
            byte[] privateKeyBytes = Base64.getDecoder().decode(devPrivateKeyStr);
            PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
            RSAPrivateKey privateKey = (RSAPrivateKey) keyFactory.generatePrivate(privateKeySpec);

            return new RSAKey.Builder(publicKey)
                    .privateKey(privateKey)
                    .keyID("84bb153c71ad35dc88aa7e5c94c3a745843d50b016341d1da19fabc63026c6d9")
                    .build();
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
    }
}
