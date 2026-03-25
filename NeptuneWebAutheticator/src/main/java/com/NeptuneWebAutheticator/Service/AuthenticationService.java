package com.NeptuneWebAutheticator.Service;

import com.NeptuneWebAutheticator.Entity.Tenant;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface AuthenticationService {
    boolean isUserAuthenticate(HttpServletRequest request);

    Tenant getUserAuthenticated(HttpServletRequest request);

    ResponseEntity<String> login(HttpServletRequest request,String username, String password);
}
