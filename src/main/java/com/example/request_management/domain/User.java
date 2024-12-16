package com.example.request_management.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    public enum Role{
        ADMIN, USER, OPERATOR
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NonNull
    private String password;
    @Column(unique = true)
    private String username;
    private String name;
    @Enumerated(EnumType.STRING)
    private Role role;
}
