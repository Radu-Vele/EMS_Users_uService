package com.usersus.services;

import com.usersus.dtos.JwtRequestDto;
import com.usersus.dtos.JwtResponseDto;
import com.usersus.dtos.UserDetailsDto;
import com.usersus.dtos.UserSignupDto;
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
import org.springframework.web.reactive.function.client.WebClient;

import java.util.*;

import static com.usersus.constants.ExceptionsConstants.USER_ALREADY_REGISTERED_MESSAGE;
import static com.usersus.constants.NetworkConstants.DEVICES_MICROSERVICE_URL;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ModelMapper mapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final WebClient.Builder webClientBuilder;

    public UUID register(UserSignupDto userDto, boolean isAdmin) throws UserAlreadyRegisteredException {
        if (userRepository.findByEmailAddress(userDto.getEmailAddress()).isPresent()) {
            throw new UserAlreadyRegisteredException(USER_ALREADY_REGISTERED_MESSAGE + userDto.getEmailAddress());
        }
        User newUser = mapper.map(userDto, User.class);
        newUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
        if(isAdmin) {
            newUser.setRole(UserRole.ADMIN);
        }
        UUID uuid = this.userRepository.save(newUser).getId();
        this.registerUserInDevicesUs(uuid);
        return uuid;
    }

    public void registerUserInDevicesUs(UUID id) {
        WebClient webClient = webClientBuilder.baseUrl(DEVICES_MICROSERVICE_URL)
                .build();
        webClient.post()
                .uri("/users/registerUserId?id=" + id)
                .retrieve()
                .bodyToMono(String.class)
                .block(); //todo check if I can do it better
    }

    public void deleteUserFromDevicesUs(UUID id) {
        WebClient webClient = webClientBuilder.baseUrl(DEVICES_MICROSERVICE_URL)
                .build();
        webClient.delete()
                .uri("/users/removeUserAndMapping?id=" + id)
                .retrieve()
                .bodyToMono(String.class)
                .block();
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

    public Void deleteUserByEmail(String emailAddress) {
        User user = this.userRepository.findByEmailAddress(emailAddress)
                .orElseThrow();
        this.userRepository.deleteById(user.getId());
        this.deleteUserFromDevicesUs(user.getId());
        return null;
    }

    public List<UserDetailsDto> getAllUsersDetails() {
        List<User> foundUsers = this.userRepository.findAll();
        return foundUsers.stream()
                .map(user -> mapper.map(user, UserDetailsDto.class))
                .toList();
    }

    public UUID editUser(UserDetailsDto userDetailsDto) throws NoSuchElementException {
        User currentUser = this.userRepository.findByEmailAddress(userDetailsDto.getEmailAddress())
                .orElseThrow(() ->
                        new NoSuchElementException("User not found with email: " + userDetailsDto.getEmailAddress()));

        currentUser.setFirstName(userDetailsDto.getFirstName()); // TODO: fix (prettify, what if new fields are added?)
        currentUser.setLastName(userDetailsDto.getLastName());
        currentUser.setRole(UserRole
                .valueOf(userDetailsDto
                        .getRole().toUpperCase(Locale.ROOT))); // TODO: invalidate token (to correspond to new role)
        // TODO: add edit email + password
        return currentUser.getId();
    }
}
