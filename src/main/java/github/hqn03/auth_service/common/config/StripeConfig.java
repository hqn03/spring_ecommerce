package github.hqn03.auth_service.common.config;

import com.stripe.StripeClient;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class StripeConfig {
    private final StripeProperties stripeProperties;

    @Bean
    public StripeClient stripeClient() {
        return new StripeClient(stripeProperties.getSecretKey());
    }
}

