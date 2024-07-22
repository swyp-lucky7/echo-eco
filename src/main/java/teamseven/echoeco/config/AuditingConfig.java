package teamseven.echoeco.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@Profile("!test")
@EnableJpaAuditing
public class AuditingConfig {
}
