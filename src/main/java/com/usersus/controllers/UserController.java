package com.usersus.controllers;

import com.usersus.dtos.UserDto;
import com.usersus.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @PostMapping("/registerUser")
    public ResponseEntity<UUID> registerUser(@RequestBody @Valid UserDto userDto) {
        return new ResponseEntity<>(userService.register(userDto, false),
                HttpStatus.CREATED);
    }
}
