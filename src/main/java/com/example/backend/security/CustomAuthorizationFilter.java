package com.example.backend.security;

import com.example.backend.model.UserEntity;
import com.example.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class CustomAuthorizationFilter extends OncePerRequestFilter {

    @Autowired
    private JWTGenerator jwtGenerator;

    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            if (jwtGenerator.validateToken(token)) {
                String username = jwtGenerator.getUsernameFromJWT(token);
                Optional<UserEntity> userOptional = userRepository.findByUsername(username);
                if (userOptional.isPresent()) {
                    UserEntity userEntity = userOptional.get();
                    int authenticatedUserId = userEntity.getId();
                    String requestURI = request.getRequestURI();
                    if (isUserAuthorizedClasses(requestURI, authenticatedUserId) || isUserAuthorizedEvents(requestURI, authenticatedUserId)) {
                        filterChain.doFilter(request, response);
                    } else {
                        response.sendError(HttpServletResponse.SC_FORBIDDEN, "You are not authorized to access this resource");
                    }
                } else {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "User not found");
                }
            } else {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
            }
        } else {
            filterChain.doFilter(request, response);
        }
    }

    private boolean isUserAuthorizedClasses(String requestURI, int authenticatedUserId) {
        Pattern pattern = Pattern.compile("/api/users/(\\d+)/userclasses.*");
        Matcher matcher = pattern.matcher(requestURI);
        if (matcher.matches()) {
            int userIdFromPath = Integer.parseInt(matcher.group(1));
            return userIdFromPath == authenticatedUserId;
        }
        return false;
    }

    private boolean isUserAuthorizedEvents(String requestURI, int authenticatedUserId) {
        Pattern pattern = Pattern.compile("/api/users/(\\d+)/calendarevents.*");
        Matcher matcher = pattern.matcher(requestURI);
        if (matcher.matches()) {
            int userIdFromPath = Integer.parseInt(matcher.group(1));
            return userIdFromPath == authenticatedUserId;
        }
        return false;
    }
}
