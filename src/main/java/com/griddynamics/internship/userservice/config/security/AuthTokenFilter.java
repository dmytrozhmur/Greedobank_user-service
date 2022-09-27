package com.griddynamics.internship.userservice.config.security;

import com.griddynamics.internship.userservice.model.user.UserWrapper;
import com.griddynamics.internship.userservice.component.processor.JwtProcessor;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthTokenFilter extends OncePerRequestFilter {
    public static final String AUTH_HEADER_NAME = "Authorization";
    public static final String AUTH_HEADER_PREFIX = "Bearer ";
    public static final String TOKEN_HEADER_NAME = "Token-Validity";
    private static final Logger logger =
            LoggerFactory.getLogger(AuthTokenFilter.class.getName());
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private JwtProcessor jwtProcessor;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = getToken(request);

        if(token != null) {
            try {
                String email = jwtProcessor.getEmail(token);

                UserWrapper userDetails = (UserWrapper) userDetailsService.loadUserByUsername(email);
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );
                authenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                response.setHeader(TOKEN_HEADER_NAME, "Token is valid");
            } catch (SignatureException | MalformedJwtException e) {
                response.setHeader(TOKEN_HEADER_NAME, "Token is wrong");
            } catch (ExpiredJwtException e) {
                response.setHeader(TOKEN_HEADER_NAME, "Token has expired");
            } catch (UsernameNotFoundException | IllegalArgumentException e) {
                logger.error(e.getMessage(), e);
            }
        }

        filterChain.doFilter(request, response);
    }

    private String getToken(HttpServletRequest request) {
        String authHeader = request.getHeader(AUTH_HEADER_NAME);

        if(StringUtils.hasText(authHeader) && authHeader.startsWith(AUTH_HEADER_PREFIX))
            return authHeader.substring(7);
        return null;
    }
}
