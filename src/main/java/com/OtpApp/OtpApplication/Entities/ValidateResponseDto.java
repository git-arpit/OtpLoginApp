package com.OtpApp.OtpApplication.Entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValidateResponseDto {
    private String userID;
    private String message;
    private boolean isAuthenticated;
}