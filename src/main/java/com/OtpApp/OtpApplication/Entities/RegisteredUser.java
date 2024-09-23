package com.OtpApp.OtpApplication.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "RegisteredUsersTable")
public class RegisteredUser {
    @Id
    private String userId;
    private String userPass;
    private String secret;
    private LocalDateTime recordAdded;
}
