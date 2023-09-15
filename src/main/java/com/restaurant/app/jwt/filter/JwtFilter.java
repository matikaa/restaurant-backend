package com.restaurant.app.jwt.filter;

import com.restaurant.app.jwt.service.JwtService;
import com.restaurant.app.user.service.UserInfoService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.restaurant.app.response.ConstantValues.*;
import static org.springframework.http.HttpStatus.FORBIDDEN;

public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    private final UserInfoService userInfoService;

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtFilter.class);

    public JwtFilter(JwtService jwtService, UserInfoService userInfoService) {
        this.jwtService = jwtService;
        this.userInfoService = userInfoService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = getToken(request);
            if (token.isEmpty()) {
                filterChain.doFilter(request, response);
                return;
            }

            String email = jwtService.extractEmail(token);

            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userInfoService.loadUserByUsername(email);
                if (userDetails == null) {
                    filterChain.doFilter(request, response);
                    return;
                }

                if (jwtService.validateToken(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
            filterChain.doFilter(request, response);
        } catch (JwtException e) {
            LOGGER.warn(NO_ACCESS);
            response.setStatus(FORBIDDEN.value());
            response.getWriter().write(NO_ACCESS);
        }
    }

    private String getToken(HttpServletRequest request) {
        String header = request.getHeader(AUTHORIZATION);
        if (header != null && header.startsWith(TOKEN_PREFIX)) {
            return header.substring(7);
        }

        return EMPTY_STRING;
    }


}
