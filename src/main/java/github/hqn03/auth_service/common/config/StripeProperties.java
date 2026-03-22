package github.hqn03.auth_service.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "stripe")
public class StripeProperties {
    private String secretKey;
    private String successUrl;
    private String cancelUrl;
}
