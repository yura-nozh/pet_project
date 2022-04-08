package com.example.yuriy_ivanov.web;

import com.example.yuriy_ivanov.dto.user.UserResponse;
import com.example.yuriy_ivanov.entities.User;
import com.example.yuriy_ivanov.repositories.BrandRepository;
import com.example.yuriy_ivanov.repositories.UserRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;


import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    @Autowired
    BrandRepository brandRepository;

    @BeforeEach
    void resetDB() {
        brandRepository.deleteAll();
        userRepository.deleteAll();
    }

    public User createUser(String firstName, String lastName, String email, String password) {
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPassword(password);

        userRepository.save(user);

        return user;
    }

    public void assertUserEquals(UserResponse firstUser, User secondUser) {
        assertEquals(firstUser.getFirstName(), secondUser.getFirstName());
        assertEquals(firstUser.getLastName(), secondUser.getLastName());
        assertEquals(firstUser.getEmail(), secondUser.getEmail());
    }

    @Test
    void shouldReturnUserList() throws Exception {
        createUser("John", "Doe", "dr1@mail.com", "qwerty");
        ResultActions resultActions = this.mockMvc.perform(get("/users"));
        String result = resultActions.andReturn().getResponse().getContentAsString();
        List<User> list = objectMapper.readValue(result, new TypeReference<>() {});

        resultActions.andExpect(status().isOk());
        assertEquals(1, list.size());
    }

    @Test
    void shouldReturnUserById() throws Exception {
        User user = createUser("John", "Doe", "dr1@mail.com", "qwerty");
        ResultActions resultActions = this.mockMvc.perform(get("/users/" + user.getId()));
        String result = resultActions.andReturn().getResponse().getContentAsString();
        UserResponse userResponse = objectMapper.readValue(result, new TypeReference<> () {});

        resultActions.andExpect(status().isOk());

        assertUserEquals(userResponse, user);
    }

    @Test
    void shouldCreateUser() throws Exception {
        List<User> listBefore = userRepository.findAll();
        User testUser = new User();
        testUser.setFirstName("John");
        testUser.setLastName("Doe");
        testUser.setEmail("dr1@mail.com");
        testUser.setPassword("qwerty");

        assertEquals(0, listBefore.size());

        MockHttpServletRequestBuilder requestBuilder = post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUser));
        ResultActions resultActions = this.mockMvc.perform(requestBuilder);
        String result = resultActions.andReturn().getResponse().getContentAsString();
        UserResponse newUser = objectMapper.readValue(result, new TypeReference<>() {});
        List<User> listAfter = userRepository.findAll();

        resultActions.andExpect(status().isOk());

        assertNotNull(result);
        assertUserEquals(newUser, testUser);

        assertEquals(1, listAfter.size());
    }

    @Test
    void shouldUpdateUser() throws Exception {
        User user = createUser("John1", "Doe1", "dr1@mail.com", "qwerty1");
        User testUser = new User();
        testUser.setFirstName("John2");
        testUser.setLastName("Doe2");
        testUser.setEmail("dr2@mail.com");
        testUser.setPassword("qwerty2");
        List<User> listBefore = userRepository.findAll();

        MockHttpServletRequestBuilder requestBuilder = put("/users/" + user.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUser));
        ResultActions resultActions = this.mockMvc.perform(requestBuilder);
        String result = resultActions.andReturn().getResponse().getContentAsString();
        UserResponse updateUser = objectMapper.readValue(result, new TypeReference<>() {});
        List<User> listAfter = userRepository.findAll();

        resultActions.andExpect(status().isOk());

        assertNotNull(result);
        assertUserEquals(updateUser, testUser);

        assertEquals(listBefore.size(), listAfter.size());
    }

    @Test
    void shouldDeleteUser() throws Exception {
        User user = createUser("John1", "Doe1", "dr1@mail.com", "qwerty1");

        ResultActions resultActions = this.mockMvc.perform(delete("/users/" + user.getId()));
        String result = resultActions.andReturn().getResponse().getContentAsString();
        List<User> listAfter = userRepository.findAll();

        resultActions.andExpect(status().isOk());

        assertNotNull(result);
        assertEquals("User was successfully deleted.", result);

        assertEquals(0, listAfter.size());
    }
}
