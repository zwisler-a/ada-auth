package de.zwisler.ada.auth.config;

import de.zwisler.ada.auth.service.KeySetService;
import java.security.interfaces.RSAPublicKey;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http, JwtDecoder decoder) throws Exception {
    http.csrf().disable().cors();
    http.authorizeHttpRequests(
            (authz) -> authz
                .requestMatchers("/swagger-ui/**").permitAll()
                .requestMatchers("/v3/api-docs/**").permitAll()
                .requestMatchers("/auth/**").permitAll()
                .requestMatchers("/.well-known/**").permitAll()
                .requestMatchers("/error/**").permitAll()
                .anyRequest().authenticated()
        )
        .oauth2ResourceServer()
        .jwt().decoder(decoder);

    return http.build();
  }


  @Bean
  public JwtDecoder jwtDecoder(KeySetService keySetService) {
    return NimbusJwtDecoder.withPublicKey((RSAPublicKey) keySetService.getCurrentKeyPair().publicKey()).build();
  }

}
