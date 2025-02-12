package com.example.demo.login.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
// import org.springframework.web.cors.CorsConfiguration;
// import org.springframework.web.cors.CorsConfigurationSource;
// import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
// import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    private final AuthenticationProvider authenticationProvider;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfiguration(
        JwtAuthenticationFilter jwtAuthenticationFilter,
        AuthenticationProvider authenticationProvider
    ) {
        this.authenticationProvider = authenticationProvider;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()
                        .anyRequest().authenticated())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exceptionHandling -> exceptionHandling
                    .authenticationEntryPoint((request, response, authException) -> {
                        response.setContentType("application/json");
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        Map<String, String> responseBody = new HashMap<>();
                        responseBody.put("error", "Authentication failed");
                        responseBody.put("status", String.valueOf(HttpServletResponse.SC_UNAUTHORIZED));
                        response.getOutputStream().println(new ObjectMapper().writeValueAsString(responseBody));
                    })
                    .accessDeniedHandler((request, response, accessDeniedException) -> {
                        response.setContentType("application/json");
                        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                        Map<String, String> responseBody = new HashMap<>();
                        responseBody.put("error", "Access denied");
                        responseBody.put("status", String.valueOf(HttpServletResponse.SC_FORBIDDEN));
                        response.getOutputStream().println(new ObjectMapper().writeValueAsString(responseBody));
                    })
                );

        return http.build();
    }

    // @Bean
    // CorsConfigurationSource corsConfigurationSource() {
    //     CorsConfiguration configuration = new CorsConfiguration();

    //     configuration.setAllowedOrigins(List.of("http://localhost:8082"));
    //     configuration.setAllowedMethods(List.of("GET", "POST"));
    //     configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));

    //     UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

    //     source.registerCorsConfiguration("/**", configuration);

    //     return source;
    // }
}