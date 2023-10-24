package com.usersus.controllers;

import com.usersus.dtos.JwtRequestDto;
import com.usersus.dtos.JwtResponseDto;
import com.usersus.dtos.UserDetailsDto;
import com.usersus.dtos.UserDto;
import com.usersus.exceptions.UserAlreadyRegisteredException;
import com.usersus.services.UserService;
import com.usersus.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.NoSuchElementException;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping("/registerUser")
    public ResponseEntity<UUID> registerUser(
            @RequestBody @Valid UserDto userDto) throws UserAlreadyRegisteredException {
        return new ResponseEntity<>(userService.register(userDto, false),
                HttpStatus.CREATED);
    }

    @GetMapping("/getUserDetailsByEmail")
    public ResponseEntity<UserDetailsDto> getUserByEmail(
            @RequestParam String emailAddress) throws NoSuchElementException {
        return new ResponseEntity<>(userService.getUserByEmail(emailAddress), HttpStatus.OK);
    }

    @GetMapping("/getSelfDetails")
    public ResponseEntity<UserDetailsDto> getSelfDetails(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String auth) throws NoSuchElementException {
        return new ResponseEntity<>(userService
                .getUserByEmail(jwtUtil
                        .getUsernameFromToken(auth
                                .substring(7))),
                HttpStatus.OK);
    }

    @PostMapping("/authenticate")
    @CrossOrigin
    public ResponseEntity<JwtResponseDto> authenticate(
            @Valid @RequestBody JwtRequestDto jwtRequest) throws AuthenticationException {
        return new ResponseEntity<>(userService.authenticate(jwtRequest), HttpStatus.OK);
    }
}
