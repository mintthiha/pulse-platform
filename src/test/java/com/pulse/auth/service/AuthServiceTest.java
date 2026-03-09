package com.pulse.auth.service;
import com.pulse.auth.model.User;
import com.pulse.auth.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;
    @InjectMocks
    private AuthService authService;

    // This should save the user and return the saved entity when username is available
    @Test
    void register_savesUserAndReturnsToken() {
        User savedUser = new User();
        savedUser.setUsername("testuser");
        savedUser.setPassword("hashed_password");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        User result = authService.register("testuser", "password123");
        assertThat(result.getUsername()).isEqualTo("testuser");
        assertThat(result.getPassword()).isEqualTo("hashed_password");
    }

    // This should throw an error when attempting to register with an already taken username
    @Test
    void register_throwsWhenUsernameAlreadyTaken() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(new User()));
        assertThatThrownBy(() -> authService.register("testuser", "password123"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("This username is already taken");
    }

    // This should return a JWT token when username and password are correct
    @Test
    void login_returnsTokenForValidCredentials() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("hashed_password");
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password123", "hashed_password")).thenReturn(true);
        when(jwtService.generateToken("testuser")).thenReturn("jwt_token");
        String token = authService.login("testuser", "password123");
        assertThat(token).isEqualTo("jwt_token");
    }

    // This should throw an error when the password does not match the stored hash
    @Test
    void login_throwsForInvalidPassword() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("hashed_password");
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongpassword", "hashed_password")).thenReturn(false);
        assertThatThrownBy(() -> authService.login("testuser", "wrongpassword"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid credentials");
    }

    // This should throw an error when the username does not exist in the database
    @Test
    void login_throwsWhenUserNotFound() {
        when(userRepository.findByUsername("unknown")).thenReturn(Optional.empty());
        assertThatThrownBy(() -> authService.login("unknown", "password123"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid credentials");
    }
}