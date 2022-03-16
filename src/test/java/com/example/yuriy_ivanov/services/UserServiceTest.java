package com.example.yuriy_ivanov.services;

import com.example.yuriy_ivanov.dto.user.UserRequest;
import com.example.yuriy_ivanov.dto.user.UserResponse;
import com.example.yuriy_ivanov.entities.User;
import com.example.yuriy_ivanov.repositories.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.transaction.Transactional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @AfterEach
    void resetDB() {
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

    public void assertUserEquals(User user, UserResponse testUser) {
        assertEquals(user.getFirstName(), testUser.getFirstName());
        assertEquals(user.getLastName(), testUser.getLastName());
        assertEquals(user.getEmail(), testUser.getEmail());
    }

    @Transactional
    @Test
    public void shouldCreateUser() {
        UserRequest newUser = new UserRequest("John", "Due", "mail@mail.com", "qwerty123");
        List<User> list = userRepository.findAll();
        assertEquals(0, list.size());
        UserResponse testUser = userService.create(newUser);

        assertEquals(newUser.getFirstName(), testUser.getFirstName());
        assertEquals(newUser.getLastName(), testUser.getLastName());
        assertEquals(newUser.getEmail(), testUser.getEmail());

        List<User> newList = userRepository.findAll();
        int fistSize = list.size() + 1;
        assertEquals(fistSize, newList.size());
    }

    @Transactional
    @Test
    public void shouldReturnAllUsers() {
        createUser("John", "Due", "mail@mail.com", "qwerty123");
        createUser("John1", "Due1", "mail@gmail.com", "qwerty321");
        Pageable pageable = PageRequest.of(0, 5, Sort.by(Sort.Direction.ASC, "id"));

        List<UserResponse> list = userService.all(pageable);
        assertEquals(2, list.size());
    }

    @Transactional
    @Test
    public void shouldReturnUserById() {
        User user = createUser("John", "Due", "mail@mail.com", "qwerty123");
        UserResponse responseUser = userService.findById(user.getId());

        assertUserEquals(user, responseUser);
    }

    @Transactional
    @Test
    public void shouldUpdateUser() {
        User user = createUser("John", "Due", "mail@mail.com", "qwerty123");
        UserRequest testUser = new UserRequest("John1", "Due2", "mail@gmail.com", "qwerty321");
        UserResponse userResponse = userService.update(user.getId(), testUser);

        assertEquals(user.getId(), userResponse.getId());
        assertUserEquals(user, userResponse);
    }

    @Transactional
    @Test
    public void shouldDeleteUser() {
        User user = createUser("John", "Due", "mail@mail.com", "qwerty123");
        assertNotNull(userRepository.findAll());

        userService.delete(user.getId());
        assertEquals(0, userRepository.findAll().size());
    }
}