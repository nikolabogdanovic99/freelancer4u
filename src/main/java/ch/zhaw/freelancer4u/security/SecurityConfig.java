package ch.zhaw.freelancer4u.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // CSRF für einfache Tests deaktivieren
        http.csrf(csrf -> csrf.disable());

        // ALLES erlauben – keine Authentifizierung nötig
        http.authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll()
        );

        // WICHTIG: KEIN oauth2ResourceServer mehr
        return http.build();
    }
}
