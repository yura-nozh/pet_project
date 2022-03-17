package com.example.yuriy_ivanov.services;

import com.example.yuriy_ivanov.dto.user.UserRequest;
import com.example.yuriy_ivanov.dto.user.UserResponse;
import com.example.yuriy_ivanov.entities.User;
import com.example.yuriy_ivanov.exception.ErrorMessage;
import com.example.yuriy_ivanov.exception.ServiceException;
import com.example.yuriy_ivanov.exception.TypicalError;
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

    public UserResponse findById(Long id) throws ServiceException {
        if(userRepository.findById(id).isPresent()) {
            User user = userRepository.getById(id);
            return objectMapper.convertValue(user, UserResponse.class);
        }

        throw new ServiceException("User not found", TypicalError.USER_NOT_FOUND);
    }

    public UserResponse update(Long id, UserRequest userRequest) {
        if(userRepository.findById(id).isEmpty()) {
            throw new ServiceException("User not found", TypicalError.USER_NOT_FOUND);
        }
        User user = userRepository.getById(id);

        // TODO: 17.03.2022 use mapper
        user.setEmail(userRequest.getEmail());
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setPassword(userRequest.getPassword());

        User updatedUser = userRepository.save(user);

        return objectMapper.convertValue(updatedUser, UserResponse.class);
    }

    public void delete(Long id) {
        if(userRepository.findById(id).isEmpty()) {
            throw new ServiceException("User not found", TypicalError.USER_NOT_FOUND);
        }
        userRepository.deleteById(id);
    }
}
