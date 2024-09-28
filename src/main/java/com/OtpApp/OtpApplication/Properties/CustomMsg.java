package com.OtpApp.OtpApplication.Properties;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("file:F:\\OtpApp\\OtpLoginApp\\src\\main\\java\\com\\OtpApp\\OtpApplication\\Properties\\customMsg.properties")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomMsg {

    @Value("${invalidSecret}")
    public String invalidSecret;
    @Value("${otpNaN}")
    public String otpNaN;
    @Value("${emptyParams}")
    public String emptyParams;
    @Value("${invalidUser}")
    private String invalidUser;
    @Value("${incorrectOtp}")
    private String incorrectOtp;
    @Value("${validSuccess}")
    private String validationSuccess;
    @Value("${validError}")
    private String validationError;
    @Value("${otpGenFailure}")
    private String otpGenFailure;
    @Value("${invalidParam}")
    private String invalidParam;
    @Value("${regSuccess}")
    private String regSuccess;
    @Value("${invalidCreds}")
    private String invalidCreds;
    @Value("${reactiveSuccess}")
    private String reactiveSuccess;
    @Value("${otpReused}")
    private String otpReused;

}
