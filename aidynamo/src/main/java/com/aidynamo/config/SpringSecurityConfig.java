package com.aidynamo.config;

import com.aidynamo.Security.JwtAuthenticationFilter;
import com.aidynamo.Security.JwtHelper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
@Configuration
@Slf4j
public class SpringSecurityConfig {
    private final JwtHelper jwtHelper;
    private final UserDetailsService userDetailsServicel;
    private final AuthenticationEntryPoint point;


    public SpringSecurityConfig(JwtHelper jwtHelper, UserDetailsService userDetailsServicel, AuthenticationEntryPoint point) {
        this.jwtHelper = jwtHelper;
        this.userDetailsServicel = userDetailsServicel;
        this.point = point;
    }
    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint(){
        return (request, response, authException) -> {
            response.sendError(HttpServletResponse.SC_ACCEPTED,"UnAuthorized");
        };
    }
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(){
        return new JwtAuthenticationFilter(jwtHelper,userDetailsServicel);
    }
    @Bean
    public SecurityFilterChain adminSecurityFilterChain(HttpSecurity http) throws Exception{
        http.
                csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .authorizeRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers("/admin/login","/admin/register","/admin/verify-otp").permitAll()
                        .requestMatchers(HttpMethod.OPTIONS).permitAll().requestMatchers("/error").permitAll()
                        .anyRequest().authenticated())
                .exceptionHandling(ex-> ex.authenticationEntryPoint((request, response, authException) -> {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED,"Unauthorized");
                })
                ).sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    return http.build();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
