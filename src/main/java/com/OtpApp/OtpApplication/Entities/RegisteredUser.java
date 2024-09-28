package com.OtpApp.OtpApplication.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "RegisteredUsersTable")
public class RegisteredUser {
    @Id
    private String userID;
    private String userPass;
    private String secret;
    private String recordAdded;
    private String usedOtp;
    private int invalidAttempts;
}
