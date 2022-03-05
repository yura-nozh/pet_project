package com.example.yuriy_ivanov.services;

import com.example.yuriy_ivanov.dto.UserDto.UserRequest;
import com.example.yuriy_ivanov.dto.UserDto.UserResponse;
import com.example.yuriy_ivanov.entities.User;
import com.example.yuriy_ivanov.repositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    public UserResponse create(UserRequest userRequest) {
        User user = objectMapper.convertValue(userRequest, User.class);
        userRepository.save(user);

        return objectMapper.convertValue(user, UserResponse.class);
    }

    public List<UserResponse> all(Pageable pageable) {
        List<UserResponse> users = new ArrayList<>();
        for(User user : userRepository.findAll(pageable).getContent()) {
            users.add(objectMapper.convertValue(user, UserResponse.class));
        }

        return users;
    }

    public UserResponse findById(Long id) {
        User user = userRepository.findById(id).get();

        return objectMapper.convertValue(user, UserResponse.class);
    }

    public UserResponse update(Long id, UserRequest userRequest) {
        User user = userRepository.getById(id);

        user.setEmail(userRequest.getEmail());
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setPassword(userRequest.getPassword());

        User updatedUser = userRepository.save(user);

        return objectMapper.convertValue(updatedUser, UserResponse.class);
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
    }
}
