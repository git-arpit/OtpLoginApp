package com.OtpApp.OtpApplication.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "AllUsersTable")
public class AllUsers {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID uuid;
    private String userID;
    private boolean isLocked;

    public AllUsers(String userID, boolean isLocked) {
        this.userID = userID;
        this.isLocked = isLocked;
    }
}
