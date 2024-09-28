package com.OtpApp.OtpApplication.Entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EncryptUserDto {
    private String userID;
    private String userPass;
    private String registerTimestamp;


}
