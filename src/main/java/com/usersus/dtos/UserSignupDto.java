package com.usersus.dtos;

import com.usersus.enums.UserRole;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
public class UserSignupDto {
    private UUID id;
    @NotNull
    @NotBlank
    private String lastName; //TODO: fix validation not working
    @NotNull
    @NotBlank
    private String firstName;
    @NotNull
    @NotBlank
    private String password;
    @Email
    @NotBlank
    @NotNull
    private String emailAddress;
    private UserRole role = UserRole.USER; // default value
    // TODO: check if I need specific equals and hashCode overrides
}
