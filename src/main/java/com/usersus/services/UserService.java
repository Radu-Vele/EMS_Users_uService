package com.usersus.services;

import com.usersus.dtos.JwtRequestDto;
import com.usersus.dtos.JwtResponseDto;
import com.usersus.dtos.UserDetailsDto;
import com.usersus.dtos.UserDto;
import com.usersus.entities.User;
import com.usersus.enums.UserRole;
import com.usersus.exceptions.UserAlreadyRegisteredException;
import com.usersus.repositories.UserRepository;
import com.usersus.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.UUID;

import static com.usersus.constants.ExceptionsConstants.USER_ALREADY_REGISTERED_MESSAGE;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ModelMapper mapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public UUID register(UserDto userDto, boolean isAdmin) throws UserAlreadyRegisteredException {
        if (userRepository.findByEmailAddress(userDto.getEmailAddress()).isPresent()) {
            throw new UserAlreadyRegisteredException(USER_ALREADY_REGISTERED_MESSAGE + userDto.getEmailAddress());
        }
        User newUser = mapper.map(userDto, User.class);
        newUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
        if(isAdmin) {
            newUser.setRole(UserRole.ADMIN);
        }
        return this.userRepository.save(newUser).getId();
    }

    public JwtResponseDto authenticate(JwtRequestDto jwtRequestDTO) throws AuthenticationException {
        this.authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        jwtRequestDTO.getUsername(), jwtRequestDTO.getPassword()
            )
        );

        User user = this.userRepository.findByEmailAddress(jwtRequestDTO.getUsername())
                .orElseThrow();
        return JwtResponseDto.builder()
                .token(jwtUtil.generateToken(user))
                .build();
    }

    public UserDetailsDto getUserByEmail(String emailAddress) throws NoSuchElementException {
        User user = this.userRepository.findByEmailAddress(emailAddress)
                .orElseThrow();
        return mapper.map(user, UserDetailsDto.class);
    }
}
