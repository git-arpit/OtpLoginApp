package com.OtpApp.OtpApplication.Entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.ComponentScan;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ComponentScan
public class ResponseErrorDto {
    private String userID;
    private String message;
    private String status;
}
