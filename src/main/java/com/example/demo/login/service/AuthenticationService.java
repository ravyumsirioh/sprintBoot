package com.example.demo.login.service;

import com.example.demo.login.model.User;
import com.example.demo.login.payload.LoginUserDto;
import com.example.demo.login.payload.RegisterUserDto;
import com.example.demo.login.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Value("${app.client_id}")
    private String client_id;
    @Value("${app.client_secret}")
    private String client_secret;

    public AuthenticationService(
            UserRepository userRepository,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User signup(RegisterUserDto input) {
        User user = new User()
                .setFullName(input.getFullName())
                .setEmail(input.getEmail())
                .setPassword(passwordEncoder.encode(input.getPassword()));

        return userRepository.save(user);
    }

    public User authenticate(LoginUserDto input) {
        if (!input.getClient_id().equals(client_id) || !input.getClient_secret().equals(client_secret)) {
            throw new RuntimeException("Invalid client credentials");
        }
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            input.getUsername(),
                            input.getPassword()));
        } catch (AuthenticationException ex) {
            throw new BadCredentialsException("Authentication failed", ex);
        }

        return userRepository.findByEmail(input.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}