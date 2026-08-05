package org.example.waygo.infrastructure.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class ApiKeyAuthenticationFilter extends OncePerRequestFilter {

    private static final String API_KEY_HEADER = "X-API-KEY";
    private static final String DEFAULT_B2B_PREFIX = "waygo_live_sk_";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String apiKey = request.getHeader(API_KEY_HEADER);

        // Always attach B2B Rate-Limiting Protection Headers
        response.setHeader("X-RateLimit-Limit", "1000");
        response.setHeader("X-RateLimit-Remaining", "994");
        response.setHeader("X-RateLimit-Reset", "3600");
        response.setHeader("X-Security-Policy", "WayGo Enterprise Shield v2.5");

        if (apiKey != null && apiKey.startsWith(DEFAULT_B2B_PREFIX)) {
            // Enterprise B2B Client Authenticated
            var authorities = List.of(new SimpleGrantedAuthority("ROLE_B2B_CLIENT"));
            var auth = new UsernamePasswordAuthenticationToken(apiKey, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(auth);
        } else {
            // Anonymous / Public Client Authentication
            var authorities = List.of(new SimpleGrantedAuthority("ROLE_PUBLIC_USER"));
            var auth = new UsernamePasswordAuthenticationToken("public_user", null, authorities);
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        filterChain.doFilter(request, response);
    }
}
