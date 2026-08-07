package com.NeptuneWebAutheticator.Service;

import com.NeptuneWebAutheticator.Entity.Tenant;
import com.NeptuneWebAutheticator.Entity.Token;
import com.NeptuneWebAutheticator.Repository.TenantRepository;
import com.NeptuneWebAutheticator.Repository.TokenRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

@Service
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {
    @Autowired
    private TenantRepository userRepository;

    @Autowired
    private TokenRepository tokenRepository;

    @Bean
    public PasswordEncoder passwordEncoderCustom() {
        return new BCryptPasswordEncoder();
    }


    public boolean isTokenValid(Token token) {
        log.info(token.getToken());
        log.info("" + token.getExpirationDate().toInstant() + " " + Instant.now().atZone(ZoneId.of("Europe/Rome")).toInstant());
        log.info("data token" + token.getExpirationDate().toInstant().isAfter(Instant.now().atZone(ZoneId.of("Europe/Rome")).toInstant()));
        Instant exp = token.getExpirationDate().toInstant(); // o token.getExpirationDate() se è già Instant
        Instant now = Instant.now();

        boolean valid = exp.isAfter(now);

        log.info("EXP_UTC={} NOW_UTC={} valid={}", exp, now, valid);
        log.info("EXP_Rome={} NOW_Rome={}",
                exp.atZone(ZoneId.of("Europe/Rome")),
                now.atZone(ZoneId.of("Europe/Rome")));

        return valid;
    }

    @Override
    public boolean isUserAuthenticate(HttpServletRequest request) {
        log.info("entro");
        if (request.getCookies() == null) return false;

        String session = Arrays.stream(request.getCookies())
                .filter(c -> "SESSION".equals(c.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);
        log.info("step1");
        if (session == null) return false;
        log.info("step2");
        Token token = tokenRepository.findByToken(session);
        if (token == null) return false;
        log.info("step3");
        return isTokenValid(token);
    }

    @Override
    public ResponseEntity<String> getUserRole(HttpServletRequest request) {
        Tenant tenant = getUserAuthenticated(request);
        if (tenant == null) {
            return ResponseEntity.status(404).body("Utente non configurato");
        }
        return ResponseEntity.ok(tenant.getRole());
    }

    @Override
    public Tenant getUserAuthenticated(HttpServletRequest request) {
        String session = Arrays.stream(request.getCookies())
                .filter(c -> "SESSION".equals(c.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);

        if (session == null) return null;

        Token token = tokenRepository.findByToken(session);
        if (token == null) return null;

        if (!isTokenValid(token)) {
            tokenRepository.delete(token);
            return null;
        }

        return userRepository.findById(token.getTenantId()).orElse(null);
    }

    @Override
    public ResponseEntity<String> login(HttpServletRequest request, String username, String password) {
        if (isUserAuthenticate(request)) {
            return ResponseEntity.ok().body("login effettuato con successo");
        }

        Tenant user = userRepository.findByUsername(username);

        if (user != null) {
            if (!passwordEncoderCustom().matches(password, user.getPassword())) {
                return ResponseEntity.status(401).body("Password errata");
            }

            Token token = tokenRepository.findByTenantId(user.getTenantId());
            if (token != null) {
                tokenRepository.delete(token);
            }
            Token newToken = new Token();
            newToken.setToken(UUID.randomUUID().toString());
            newToken.setExpirationDate(Date.from(Instant.now().plus(60, ChronoUnit.MINUTES)));
            newToken.setTenantId(user.getTenantId());
            tokenRepository.save(newToken);

            ResponseCookie cookie = ResponseCookie.from("SESSION", newToken.getToken())
                    .httpOnly(true)
                    .secure(true)
                    .path("/")
                    .maxAge(Duration.ofHours(1))
                    .sameSite("Lax")
                    .build();

            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, cookie.toString())
                    .body("login effettuato con successo");
        }
        return ResponseEntity.status(401).body("Utente non configurato");
    }

    @Override
    @Transactional
    public ResponseEntity<String> logout(HttpServletRequest request) {
        if (!isUserAuthenticate(request)) {
            return ResponseEntity.ok().body("logout effettuato con successo");
        }

        Tenant tenant = getUserAuthenticated(request);

        if (tenant == null) {
            return ResponseEntity.status(404).body("Logout per utente non trovato");
        }

        Token token = tokenRepository.findByTenantId(tenant.getTenantId());

        tokenRepository.delete(token);
        return ResponseEntity.ok().body("logout effettuato con successo");
    }
}
