package com.usersus.entities;

import com.usersus.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User {
    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy= GenerationType.UUID)
    @Column(columnDefinition = "BINARY(32)")
    private UUID id;
    @Getter
    @Setter
    @Column(nullable = false)
    private String lastName;
    @Getter
    @Setter
    @Column(nullable = false)
    private String firstName;
    @Getter
    @Setter
    @Column(nullable = false, unique = true)
    private String emailAddress;
    @Getter
    @Setter
    @Column(nullable = false)
    private String password;
    @Getter
    @Setter
    @ElementCollection
    private List<UUID> devices;
    @Getter
    @Setter
    @Column(nullable = false, columnDefinition = "VARCHAR(10)")
    private UserRole role;
}