package com.OtpApp.OtpApplication.Entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.ComponentScan;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ComponentScan
public class OtpBean {
    private String userID;
    private  String secret;

}
