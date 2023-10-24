package com.usersus.dtos;

import lombok.Data;

@Data
public class UserDetailsDto {
    private String firstName;
    private String lastName;
    private String emailAddress;
    private String role;
}
