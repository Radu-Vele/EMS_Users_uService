package com.usersus.services;

import com.usersus.dtos.JwtRequestDto;
import com.usersus.dtos.JwtResponseDto;
import com.usersus.dtos.UserDto;
import com.usersus.entities.User;
import com.usersus.enums.UserRole;
import com.usersus.repositories.UserRepository;
import com.usersus.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ModelMapper mapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public UUID register(UserDto userDto, boolean isAdmin) {
        //TODO: check if exists - first check what happens if I try to insert it (email set as unique)
        User newUser = mapper.map(userDto, User.class);
        newUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
        if(isAdmin) {
            newUser.setRole(UserRole.ADMIN);
        }
        return this.userRepository.save(newUser).getId();
    }

    public JwtResponseDto authenticate(JwtRequestDto jwtRequestDTO) throws Exception{
        try {
            this.authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            jwtRequestDTO.getUsername(),
                            jwtRequestDTO.getPassword()
                    )
            );
        } catch (Exception e) {
            System.out.println(e.getMessage());  // TODO: handle all exceptions in the handler
            return null;
        }
        User user = this.userRepository.findByEmailAddress(jwtRequestDTO.getUsername())
                .orElseThrow();
        return JwtResponseDto.builder()
                .token(jwtUtil.generateToken(user))
                .build();
    }
}
