package com.test.payment.account.domain.user.service;

import com.test.payment.account.domain.user.User;
import com.test.payment.account.domain.user.repository.UserRepository;
import com.test.payment.account.dto.UserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUsername("testuser");
        user.setPassword("testpassword");
        user.setEnabled(true);
    }

    @Test
    void testSaveUser() {
        when(passwordEncoder.encode(anyString())).thenReturn("encodedpassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserDTO userDTO = userService.saveUser(user);

        assertNotNull(userDTO);
        assertEquals("testuser", userDTO.getUsername());
        assertTrue(userDTO.isEnabled());
        verify(passwordEncoder, times(1)).encode(anyString());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testFindByUsername_UserFound() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        UserDTO userDTO = userService.findByUsername("testuser");

        assertNotNull(userDTO);
        assertEquals("testuser", userDTO.getUsername());
        assertTrue(userDTO.isEnabled());
        verify(userRepository, times(1)).findByUsername("testuser");
    }

    @Test
    void testFindByUsername_UserNotFound() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());

        Exception exception = assertThrows(UsernameNotFoundException.class, () -> {
            userService.findByUsername("testuser");
        });

        assertEquals("User not found with username: testuser", exception.getMessage());
        verify(userRepository, times(1)).findByUsername("testuser");
    }

    @Test
    void testUpdateUser_UserFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(passwordEncoder.encode(anyString())).thenReturn("encodedpassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        User updatedUser = new User();
        updatedUser.setUsername("updateduser");
        updatedUser.setPassword("updatedpassword");
        updatedUser.setEnabled(false);

        UserDTO userDTO = userService.updateUser(1L, updatedUser);

        assertNotNull(userDTO);
        assertEquals("updateduser", userDTO.getUsername());
        assertFalse(userDTO.isEnabled());
        verify(userRepository, times(1)).findById(anyLong());
        verify(passwordEncoder, times(1)).encode(anyString());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testUpdateUser_UserNotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        User updatedUser = new User();
        updatedUser.setUsername("updateduser");
        updatedUser.setPassword("updatedpassword");
        updatedUser.setEnabled(false);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            userService.updateUser(1L, updatedUser);
        });

        assertEquals("User not found", exception.getMessage());
        verify(userRepository, times(1)).findById(anyLong());
        verify(userRepository, times(0)).save(any(User.class));
    }

    @Test
    void testDeleteUser() {
        doNothing().when(userRepository).deleteById(anyLong());

        userService.deleteUser(1L);

        verify(userRepository, times(1)).deleteById(anyLong());
    }
}

