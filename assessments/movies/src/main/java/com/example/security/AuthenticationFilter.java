package com.example.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthenticationFilter extends OncePerRequestFilter {

    private final String token = "18394928-6684-43ca-9026-e19ed3028b17";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String bearerToken = request.getHeader("Authorization");

        if (bearerToken == null) {
            throw new SecurityException("No token provided");
        }

        if (!bearerToken.equals(String.format("Bearer %s", token))) {
            throw new SecurityException("Incorrect token");
        }

        Authentication auth = new MovieAuth();
        SecurityContextHolder.getContext().setAuthentication(auth);

        filterChain.doFilter(request, response);
    }

}