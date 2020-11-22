package com.spotify.api.config;

import com.spotify.api.service.UserDetailsService;
import com.spotify.api.util.JwtTokenUtil;
import com.spotify.api.util.StringOperations;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.experimental.ExtensionMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;

/**
 * Source: https://medium.com/swlh/spring-boot-security-jwt-hello-world-example-b479e457664c
 * **/
@Component
@ExtensionMethod({StringOperations.class})
public class JwtRequestFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;
    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    public JwtRequestFilter(UserDetailsService userDetailsService, JwtTokenUtil jwtTokenUtil) {
        this.userDetailsService = userDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        final String requestTokenHeader = httpServletRequest.getHeader("Authorization");
        String jwtToken = null;
        String email = null;
        if(requestTokenHeader != null && requestTokenHeader.toLowerCase().startsWith("bearer")){
            jwtToken = requestTokenHeader.substring(7);
            email = _setHelper(jwtToken);
        }
        else{
            String _params = httpServletRequest.getQueryString();
            Map<String, String> queryParams = StringOperations.toMap(_params);
            boolean check =
                    Objects.nonNull(queryParams.get("type"))
                    && queryParams.get("type").equalsIgnoreCase("websocket")
                    && Objects.nonNull(queryParams.get("token"));
            if(check){
                jwtToken = queryParams.get("token");
                email = _setHelper(jwtToken);
            }
        }

        if(email != null && SecurityContextHolder.getContext().getAuthentication() == null){
            UserDetails user = userDetailsService.loadUserByUsername(email);
            if(jwtTokenUtil.validateToken(jwtToken, user)){
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    private String _setHelper(String jwtToken){
        String email = null;
        try{
            email = jwtTokenUtil.getEmailFromToken(jwtToken);
        }catch (IllegalArgumentException ex){
            System.out.println("Unable to get JWT Token.");
        }catch (ExpiredJwtException ex){
            System.out.println("JWT has expired.");
        }
        return email;
    }
}
