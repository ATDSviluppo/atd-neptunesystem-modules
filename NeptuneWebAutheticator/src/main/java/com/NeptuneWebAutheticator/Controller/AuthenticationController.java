package com.NeptuneWebAutheticator.Controller;

import com.NeptuneWebAutheticator.Service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class AuthenticationController {
    @Autowired
    private AuthenticationService authenticationService;

    //@CrossOrigin(origins = "http://localhost:5555", allowCredentials = "true")
    @PostMapping("/api/login")
    public ResponseEntity<String> login(HttpServletRequest request, @RequestBody Map<String, String> payload) {
        String username = payload.get("username");
        String password = payload.get("password");
        log.info("" + payload);
        return authenticationService.login(request, username, password);
    }

    //@CrossOrigin(origins = "http://localhost:5555", allowCredentials = "true")
    @GetMapping("/api/isUserAuthenticate")
    public boolean isUserAuthenticate(HttpServletRequest userId) {
        log.info("entro");
        return authenticationService.isUserAuthenticate(userId);
    }

    //@CrossOrigin(origins = "http://localhost:5555", allowCredentials = "true")
    @GetMapping("/api/logout")
    public boolean logout(HttpServletRequest userId) {
        log.info("entro");
        return authenticationService.isUserAuthenticate(userId);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(List.of("http://localhost:*"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
