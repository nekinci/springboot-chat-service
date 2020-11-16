package com.spotify.api.config;

import com.spotify.api.models.User;
import com.spotify.api.service.UserService;
import com.spotify.api.util.JwtTokenUtil;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Source: https://medium.com/swlh/spring-boot-security-jwt-hello-world-example-b479e457664c
 * **/
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private UserService userService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        final String requestTokenHeader = httpServletRequest.getHeader("Authorization");
        String jwtToken = null;
        String email = null;
        if(requestTokenHeader != null && requestTokenHeader.toLowerCase().startsWith("bearer")){
            jwtToken = requestTokenHeader.substring(7);
            try{
                email = jwtTokenUtil.getEmailFromToken(jwtToken);
            }catch (IllegalArgumentException ex){
                System.out.println("Unable to get JWT Token.");
            }catch (ExpiredJwtException ex){
                System.out.println("JWT has expired.");
            }
        }
        else{
            logger.warn("JWT doesn't begin with Bearer string.");
        }

        if(email != null && SecurityContextHolder.getContext().getAuthentication() == null){
            User user = this.userService.getUserByEmail(email);
            if(jwtTokenUtil.validateToken(jwtToken, user)){
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(user, null);
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
