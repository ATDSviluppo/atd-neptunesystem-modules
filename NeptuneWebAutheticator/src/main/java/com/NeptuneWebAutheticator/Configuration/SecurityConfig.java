package com.NeptuneWebAutheticator.Configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .cors(cors -> cors.disable())
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/api/sendMail").permitAll()
                        .requestMatchers("/api/login", "/api/getUserRoleAuthenticated", "/api/isUserAuthenticate", "/Machine", "/api/Tenant", "/api/logout", "/api/modules", "/api/modules/{jarName}").permitAll()
                        .anyRequest().authenticated()
                )
                .build();
    }
}
