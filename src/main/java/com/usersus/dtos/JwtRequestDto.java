package com.usersus.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class JwtRequestDto {
    @NotBlank
    @NotNull
    private String username;
    @NotBlank
    @NotNull
    private String password;
}
