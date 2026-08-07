package com.NeptuneWebAutheticator.Controller;

import com.NeptuneWebAutheticator.Service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
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
        return authenticationService.login(request, username, password);
    }

    //@CrossOrigin(origins = "http://localhost:5555", allowCredentials = "true")
    @GetMapping("/api/isUserAuthenticate")
    public boolean isUserAuthenticate(HttpServletRequest userId) {
        return authenticationService.isUserAuthenticate(userId);
    }

    @GetMapping("/api/getUserRoleAuthenticated")
    public ResponseEntity<String> getUserRoleAuthenticated(HttpServletRequest userId) {
        return authenticationService.getUserRole(userId);
    }

    //@CrossOrigin(origins = "http://localhost:5555", allowCredentials = "true")
    @PostMapping("/api/logout")
    public ResponseEntity<String> logout(HttpServletRequest userId) {
        return authenticationService.logout(userId);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(List.of("http://localhost:[*]", "https://neptunesystem.zcsautomation.com"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
