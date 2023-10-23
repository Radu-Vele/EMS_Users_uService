package com.usersus.dtos;

import com.usersus.enums.UserRole;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
public class UserDto {
    private UUID id;
    @NotNull
    private String lastName;
    @NotNull
    private String firstName;
    @NotNull
    private String password;
    @Email
    private String emailAddress;
    private UserRole role = UserRole.USER; // default value
    // TODO: check if I need specific equals and hashCode overrides
}
