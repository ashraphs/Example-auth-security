package com.example.Exampleauthsecurity.securities;

import com.example.Exampleauthsecurity.services.CustomUserDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class AuthenticationFilter extends OncePerRequestFilter {


    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    CustomUserDetailsService customUserDetailsService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try {
            String token = getJwtFromRequest(request);

            if (token != null && tokenProvider.validateToken(token)) {
                Long userId = tokenProvider.getUserIdFromJWT(token);

                UserDetails userDetails = customUserDetailsService.loadUserById(userId);
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }

        } catch (Exception ex) {
            log.info("Security Context user authentication: ", ex);
        } finally {
            filterChain.doFilter(request, response);
        }
    }

    @Nullable
    private String getJwtFromRequest(HttpServletRequest request) {

        log.info("Auth Filter-Requst: {}", request.getPathInfo());

        String token = request.getHeader("Authorization");
        logger.info("Token: " + token);

        if (StringUtils.hasText(token) && token.startsWith("Bearer")) {
            return token.substring(7);
        }

        return null;
    }
}
