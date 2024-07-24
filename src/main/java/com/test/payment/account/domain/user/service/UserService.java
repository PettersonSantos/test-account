package com.test.payment.account.domain.user.service;

import com.test.payment.account.domain.user.Role;
import com.test.payment.account.domain.user.User;
import com.test.payment.account.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.test.payment.account.domain.user.repository.UserRepository;
import com.test.payment.account.domain.user.repository.RoleRepository;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserDTO saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User userSaved = userRepository.save(user);
        return convertToDTO(userSaved);
    }

    public Role saveRole(Role role) {
        return roleRepository.save(role);
    }

    public UserDTO findByUsername(String username) {
        User user = userRepository.findByUsername(username);
        return convertToDTO(user);
    }

    public UserDTO updateUser(Long id, User user) {
        Optional<User> existingUserOptional = userRepository.findById(id);
        if (existingUserOptional.isPresent()) {
            User existingUser = existingUserOptional.get();
            existingUser.setUsername(user.getUsername());
            existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
            existingUser.setEnabled(user.isEnabled());
            existingUser.setRole(user.getRole());
            User userUpdated = userRepository.save(existingUser);
            return convertToDTO(userUpdated);
        } else {
            throw new RuntimeException("User not found");
        }
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    private UserDTO convertToDTO(User user) {
        return new UserDTO(user.getUsername(), user.isEnabled());
    }
}
