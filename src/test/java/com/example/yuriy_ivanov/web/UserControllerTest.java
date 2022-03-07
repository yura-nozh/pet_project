package com.example.yuriy_ivanov.web;

import com.example.yuriy_ivanov.dto.user_dto.UserResponse;
import com.example.yuriy_ivanov.entities.User;
import com.example.yuriy_ivanov.repositories.UserRepository;
import com.example.yuriy_ivanov.services.UserService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;


import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    public User createUser(String firstName, String lastName, String email, String password) {
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPassword(password);

        userRepository.save(user);

        return user;
    }

    @AfterEach
    public void resetDB() {
        userRepository.deleteAll();
    }

    public void assertUserEquals(User firstUser, User secondUser) {
        assertEquals(firstUser.getFirstName(), secondUser.getFirstName());
        assertEquals(firstUser.getLastName(), secondUser.getLastName());
        assertEquals(firstUser.getEmail(), secondUser.getEmail());
        assertEquals(1, firstUser.getId());
    }

    @Test
    public void shouldReturnUserList() throws Exception {
        createUser("John", "Doe", "dr1@mail.com", "qwerty");
        ResultActions resultActions = this.mockMvc.perform(get("/users"));
        String result = resultActions.andReturn().getResponse().getContentAsString();
        List<User> list = objectMapper.readValue(result, new TypeReference<>() {});

        resultActions.andExpect(status().isOk());
        assertEquals(1, list.size());
    }

    @Test
    public void shouldReturnUserById() throws Exception {
        createUser("John", "Doe", "dr1@mail.com", "qwerty");
        ResultActions resultActions = this.mockMvc.perform(get("/users/1"));
        String result = resultActions.andReturn().getResponse().getContentAsString();
        UserResponse userResponse = objectMapper.readValue(result, new TypeReference<> () {});

        resultActions.andExpect(status().isOk());

        assertEquals(1, userResponse.getId());
    }

    @Test
    public void shouldCreateUser() throws Exception {
        List<User> usersBefore = userRepository.findAll();
        User testUser = new User();
        testUser.setFirstName("John");
        testUser.setLastName("Doe");
        testUser.setEmail("dr1@mail.com");
        testUser.setPassword("qwerty");

        assertEquals(0, usersBefore.size());

        MockHttpServletRequestBuilder requestBuilder = post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUser));
        ResultActions resultActions = this.mockMvc.perform(requestBuilder);
        String result = resultActions.andReturn().getResponse().getContentAsString();
        User newUser = objectMapper.readValue(result, new TypeReference<>() {});
        List<User> userAfter = userRepository.findAll();

        resultActions.andExpect(status().isOk());

        assertNotNull(result);
        assertUserEquals(newUser, testUser);

        assertEquals(1, userAfter.size());
    }

    @Test
    public void shouldUpdateUser() throws Exception {
        createUser("John1", "Doe1", "dr1@mail.com", "qwerty1");
        User testUser = new User();
        testUser.setFirstName("John2");
        testUser.setLastName("Doe2");
        testUser.setEmail("dr2@mail.com");
        testUser.setPassword("qwerty2");
        List<User> usersBefore = userRepository.findAll();

        MockHttpServletRequestBuilder requestBuilder = put("/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUser));
        ResultActions resultActions = this.mockMvc.perform(requestBuilder);
        String result = resultActions.andReturn().getResponse().getContentAsString();
        User updateUser = objectMapper.readValue(result, new TypeReference<>() {});
        List<User> userAfter = userRepository.findAll();

        resultActions.andExpect(status().isOk());

        assertNotNull(result);
        assertUserEquals(updateUser, testUser);

        assertEquals(usersBefore.size(), userAfter.size());
    }

    @Test
    public void shouldDeleteUser() throws Exception {
        createUser("John1", "Doe1", "dr1@mail.com", "qwerty1");

        ResultActions resultActions = this.mockMvc.perform(delete("/users/1"));
        String result = resultActions.andReturn().getResponse().getContentAsString();
        List<User> listAfter = userRepository.findAll();

        resultActions.andExpect(status().isOk());

        assertNotNull(result);
        assertEquals("User was successfully deleted.", result);

        assertEquals(0, listAfter.size());
    }

}
