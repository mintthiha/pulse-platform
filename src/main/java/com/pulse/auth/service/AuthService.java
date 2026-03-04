package com.pulse.auth.service;

import com.pulse.auth.model.User;
import com.pulse.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    /**
     * Registers a new user by encoding their password and saving them to the database.
     * Throws an exception if the username is already taken.
     *
     * @param username the desired username
     * @param password the plain text password to be hashed
     * @return the saved User entity
     */
    public User register(String username, String password) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("This username is already taken");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setCreatedAt(java.time.LocalDateTime.now());

        return userRepository.save(user);
    }

    /**
     * Authenticates a user by verifying their password against the stored hash.
     * Throws an exception if credentials are invalid.
     *
     * @param username the username to authenticate
     * @param password the plain text password to verify
     * @return a signed JWT token string
     */
    public String login(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("Invalid credentials");
        }

        return jwtService.generateToken(username);
    }
}