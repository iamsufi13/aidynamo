package com.aidynamo.Security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.antlr.v4.runtime.Token;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    private final JwtHelper jwtHelper;
    private final UserDetailsService userDetailsService;
    private static final String TOKEN_PREFIX= "Bearer ";

    public JwtAuthenticationFilter(JwtHelper jwtHelper, UserDetailsService userDetailsService) {
        this.jwtHelper = jwtHelper;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(){
        return new JwtAuthenticationFilter(jwtHelper,userDetailsService);
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String requestPath = request.getServletPath();
        if (requestPath.startsWith("/admin")){
            filterChain.doFilter(request,response);
            return;
        }

        String requestHeader = request.getHeader("Authorization");
        logger.debug("Authorization Header : {}", requestHeader);

        if (requestHeader != null && requestHeader.startsWith(TOKEN_PREFIX)){
            String token = requestHeader.substring(TOKEN_PREFIX.length());
            logger.debug("Extracted Token: {}",token);


        try {
            String username =  jwtHelper.getUsernameFromToken(token);
            logger.debug("Extracted token : {}",token);

            if (username!= null && SecurityContextHolder.getContext().getAuthentication()== null){
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            logger.debug("LOADED UserDetails : {}",userDetails);

            if (jwtHelper.validateToken(token,userDetails)){
                logger.debug("Token is valid for user : {}", username);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null,userDetails.getAuthorities()
                );
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            else {
                logger.warn("Invalidate Jwt token for user: {}",username);
            }
            }
            else {logger.warn("Username not found in token or already authenticated");}
        }catch (Exception e){
            logger.error("Jwt Authentication Failed: ",e);
        }

        }
        else {
            logger.warn("No Jwt token found in the request");
        }
        filterChain.doFilter(request,response);

    }
}
