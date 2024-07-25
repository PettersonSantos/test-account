package com.test.payment.account.domain.user.controller;

import com.test.payment.account.IntegrationTestBase;
import com.test.payment.account.domain.user.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest extends IntegrationTestBase {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getUserByUsernameTest() throws Exception {
        String token = obtainAccessToken("testuser", "testpassword");

        mockMvc.perform(get("/api/users/testuser")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testuser"));
    }

    @Test
    void createUserTest() throws Exception {
        User newUser = new User();
        newUser.setUsername("newuser");
        newUser.setPassword("newpassword");
        String userJson = "{ \"username\": \"newuser\", \"password\": \"newpassword\" }";

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("newuser"));
    }

    @Test
    void updateUserTest() throws Exception {
        String token = obtainAccessToken("testuser", "testpassword");

        Optional<User> existingUserOptional = userRepository.findByUsername("testuser");
        assertTrue(existingUserOptional.isPresent(), "User not found");
        User existingUser = existingUserOptional.get();

        String updateJson = "{ \"username\": \"updateduser\", \"password\": \"updatedpassword\" }";

        mockMvc.perform(put("/api/users/" + existingUser.getId())
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("updateduser"));
    }

    @Test
    void deleteUserTest() throws Exception {
        String token = obtainAccessToken("testuser", "testpassword");

        Optional<User> existingUserOptional = userRepository.findByUsername("testuser");
        assertTrue(existingUserOptional.isPresent(), "User not found");
        User existingUser = existingUserOptional.get();

        mockMvc.perform(delete("/api/users/" + existingUser.getId())
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNoContent());
    }
}