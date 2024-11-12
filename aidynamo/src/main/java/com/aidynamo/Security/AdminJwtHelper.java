package com.aidynamo.Security;

import com.aidynamo.admin.Admin;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
public class AdminJwtHelper extends JwtHelper {

    private UserDetails userDetails;


    public boolean validateToken(String token, Admin userDetails) {
        String username = getUsernameFromToken(token);
        System.out.println("***********************************************");
        log.warn("User info {}", username);
        System.out.println("***********************************************");
        log.debug("Validating token for user: {}", username);
        log.debug("UserDetails Username: {}", userDetails.getUsername());

        boolean isValid = username.equals(userDetails.getUsername()) && !isTokenExpired(token);

        if (!isValid) {
            log.warn("Token is invalid for user: {}", username);
        }

        return isValid;
    }
}
