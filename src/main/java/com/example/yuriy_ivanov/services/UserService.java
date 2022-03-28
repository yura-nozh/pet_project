package com.example.yuriy_ivanov.services;

import com.example.yuriy_ivanov.dto.addresses.AddressesRequest;
import com.example.yuriy_ivanov.dto.addresses.AddressesResponse;
import com.example.yuriy_ivanov.dto.user.UserRequest;
import com.example.yuriy_ivanov.dto.user.UserResponse;
import com.example.yuriy_ivanov.entities.Addresses;
import com.example.yuriy_ivanov.entities.User;
import com.example.yuriy_ivanov.exception.ServiceException;
import com.example.yuriy_ivanov.exception.TypicalError;
import com.example.yuriy_ivanov.repositories.AddressesRepository;
import com.example.yuriy_ivanov.repositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;
    private final ModelMapper mapper;
    private final AddressesRepository addressesRepository;

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

        // FIXED
        User user = mapper.map(userRequest, User.class);
        user.setId(id);
        User updatedUser = userRepository.save(user);

        return objectMapper.convertValue(updatedUser, UserResponse.class);
    }

    public void delete(Long id) {
        if(userRepository.findById(id).isEmpty()) {
            throw new ServiceException("User not found", TypicalError.USER_NOT_FOUND);
        }
        userRepository.deleteById(id);
    }

    public AddressesResponse addAddressToUser(AddressesRequest addressesRequest) {
        Addresses address = objectMapper.convertValue(addressesRequest, Addresses.class);
        addressesRepository.save(address);
        return objectMapper.convertValue(addressesRequest, AddressesResponse.class);
    }
}
