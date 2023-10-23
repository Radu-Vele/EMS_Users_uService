package com.usersus.services;

import com.usersus.dtos.UserDto;
import com.usersus.entities.User;
import com.usersus.enums.UserRole;
import com.usersus.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ModelMapper mapper;

    public UUID register(UserDto userDto, boolean isAdmin) {
        User newUser = mapper.map(userDto, User.class);
        if(isAdmin) {
            newUser.setRole(UserRole.ADMIN);
        }
        return this.userRepository.save(newUser).getId();
    }
}
