package com.test.payment.account.domain.user.controller;

import com.test.payment.account.domain.user.User;
import com.test.payment.account.dto.UserDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.test.payment.account.domain.user.service.UserService;

import java.util.List;


@RestController
@RequestMapping("/api/users")
@Tag(name = "User Management System", description = "Operations pertaining to users in User Management System")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{name}")
    @Operation(summary = "Get a user by Username", description = "Get user details by username", tags = { "user" })
    public ResponseEntity<UserDTO> getUserByUsername(@PathVariable String name) {
        UserDTO user = userService.findByUsername(name);
        return ResponseEntity.ok(user);
    }

    @PostMapping
    @Operation(summary = "Create a new user", description = "Create a new user and return the created user details", tags = { "user" })
    public ResponseEntity<UserDTO> createUser(@RequestBody User user) {
        UserDTO newUser = userService.saveUser(user);
        return ResponseEntity.ok(newUser);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing user", description = "Update an existing user by ID and return the updated user details", tags = { "user" })
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody User user) {
        UserDTO updatedUser = userService.updateUser(id, user);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a user", description = "Delete a user by ID", tags = { "user" })
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
