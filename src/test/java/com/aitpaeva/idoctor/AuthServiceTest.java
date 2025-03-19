package com.aitpaeva.idoctor;

import com.aitpaeva.idoctor.model.Role;
import com.aitpaeva.idoctor.model.User;
import com.aitpaeva.idoctor.repository.UserRepository;
import com.aitpaeva.idoctor.service.AuthService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private UserRepository userRepository;

    @Test
    void testRegisterUser() {
        User user = new User(1L, "testuser", "password", Role.PATIENT);
        when(userRepository.save(any(User.class))).thenReturn(user);

        String result = authService.register(user);

        assertEquals("User registered successfully", result);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testLoginUser() {
        User user = new User(1L, "testuser", "password", Role.PATIENT);
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        String token = authService.login("testuser", "password");

        assertEquals("mocked-jwt-token", token);
        verify(userRepository, times(1)).findByUsername("testuser");
    }
}
