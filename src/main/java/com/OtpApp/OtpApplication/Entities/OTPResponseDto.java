package com.OtpApp.OtpApplication.Entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.ComponentScan;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ComponentScan
public class OTPResponseDto {
    private String message;
    private String otp;
    private String userID;
}
